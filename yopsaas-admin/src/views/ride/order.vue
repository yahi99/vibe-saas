<template>
  <div class="app-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input v-model="listQuery.userId" clearable class="filter-item" style="width: 160px;" placeholder="请输入用户ID" />
      <el-input v-model="listQuery.rideOrderId" clearable class="filter-item" style="width: 160px;" placeholder="请输入订单编号" />
      <el-date-picker v-model="listQuery.timeArray" type="datetimerange" class="filter-item" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" :picker-options="pickerOptions" />
      <el-select v-model="listQuery.rideOrderStatusArray" multiple style="width: 200px" class="filter-item" placeholder="请选择订单状态">
        <el-option v-for="(key, value) in statusMap" :key="key" :label="key" :value="value" />
      </el-select>
      <el-button v-permission="['GET /admin/rideorder/list']" class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">导出</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table v-loading="listLoading" :data="list" element-loading-text="正在查询中。。。" border fit highlight-current-row>

      <el-table-column align="center" min-width="100" label="订单编号" prop="rideOrderId" />

      <el-table-column align="center" label="用户ID" prop="userId" />

      <el-table-column align="center" label="第三方订单ID" prop="ycOrderId" />

      <el-table-column align="center" label="产品类型" prop="productTypeId">
        <template slot-scope="scope">
          {{ scope.row.productTypeId | productFilter }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="用车时间" prop="startTime">
        <template slot-scope="scope">
          {{ scope.row.startTime | timeFilter }}
        </template>
      </el-table-column>
      <el-table-column width="200" align="center" label="上车地点" prop="startAddress" />
      <el-table-column width="200" align="center" label="下车地点" prop="endAddress" />

      <el-table-column align="center" label="订单状态" prop="status">
        <template slot-scope="scope">
          {{ scope.row.status | statusFilter }}
        </template>
      </el-table-column>

      <el-table-column align="center" label="订单金额" prop="totalAmount" />
      <el-table-column align="center" label="支付金额" prop="deposit" />
      <el-table-column align="center" label="支付时间" prop="payTime" />

      <el-table-column align="center" label="操作" width="160" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-permission="['GET /admin/rideorder/detail']" type="primary" size="mini" @click="handleDetail(scope.row)">详情</el-button>
          <el-button v-permission="['POST /admin/rideorder/delete']" type="danger" size="mini" @click="handleDelete(scope.row)">删除</el-button>
          <el-button v-if="scope.row.payStatus==3" v-permission="['POST /admin/rideorder/refund']" type="primary" size="mini" @click="handleRefund(scope.row)">退款</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <!-- 订单详情对话框 -->
    <el-dialog :visible.sync="orderDialogVisible" title="订单详情" width="800">
      <section ref="print">
        <el-form :data="orderDetail" label-position="left">
          <el-form-item label="订单编号">
            <span>{{ orderDetail.order.rideOrderId }}</span>
          </el-form-item>
          <el-form-item label="订单状态">
            <el-tag>{{ orderDetail.order.status | statusFilter }}</el-tag>
          </el-form-item>
          <el-form-item label="订单用户">
            <span>{{ orderDetail.user.nickname }}</span>
          </el-form-item>
          <el-form-item label="用户留言">
            <span>{{ orderDetail.order.message }}</span>
          </el-form-item>
          <el-form-item label="订单信息">
            <span>（上车地点）{{ orderDetail.order.start_position }}</span>
            <span>（手机号）{{ orderDetail.order.passenger_phone }}</span>
          </el-form-item>
          <el-form-item label="费用信息">
            <span>
              (实际费用){{ orderDetail.order.totalAmount }}元 =
              (商品总价){{ orderDetail.order.originAmount }}元 -
              (优惠减免){{ orderDetail.order.couponFacevalue }}元
            </span>
          </el-form-item>
          <el-form-item label="支付信息">
            <span>（支付渠道）微信支付</span>
            <span>（支付时间）{{ orderDetail.order.payTime }}</span>
          </el-form-item>
          <el-form-item label="退款信息">
            <span>（退款金额）{{ orderDetail.order.refundAmount }}元</span>
            <span>（退款类型）{{ orderDetail.order.refundType }}</span>
            <span>（退款备注）{{ orderDetail.order.refundContent }}</span>
            <span>（退款时间）{{ orderDetail.order.refundTime }}</span>
          </el-form-item>
        </el-form>
      </section>
      <span slot="footer" class="dialog-footer">
        <el-button @click="orderDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="printOrder">打 印</el-button>
      </span>
    </el-dialog>

    <!-- 退款对话框 -->
    <el-dialog :visible.sync="refundDialogVisible" title="退款">
      <el-form ref="refundForm" :model="refundForm" status-icon label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">
        <el-form-item label="退款金额" prop="refundMoney">
          <el-input v-model="refundForm.refundMoney" :disabled="true" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="refundDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmRefund">确定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { detailOrder, listOrder, refundOrder, deleteOrder } from '@/api/rideorder'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import checkPermission from '@/utils/permission' // 权限判断函数

const statusMap = {
  0: '未初始化',
  1: '等待用户付款',
  2: '等待分配车辆',
  3: '等待司机确认',
  4: '等待司机就位',
  5: '等待服务开始',
  6: '等待服务结束',
  7: '服务结束',
  8: '订单取消'
}
const productMap = {
  1: '预约',
  17: '马上用车'
}

export default {
  name: 'RideOrder',
  components: { Pagination },
  filters: {
    statusFilter(status) {
      return statusMap[status]
    },
    productFilter(productTypeId) {
      return productMap[productTypeId]
    },
    timeFilter(time) {
      var date1 = new Date(time * 1000)
      var y = date1.getFullYear()
      var m = date1.getMonth() + 1
      var d = date1.getDate()
      var date = y + '-' + (m < 10 ? '0' + m : m) + '-' + (d < 10 ? '0' + d : d) + ' ' + date1.toTimeString().substr(0, 8)
      return date
    }
  },
  data() {
    return {
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        rideOrderId: undefined,
        timeArray: [],
        rideOrderStatusArray: [],
        sort: 'ride_order_id',
        order: 'desc'
      },
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            picker.$emit('pick', [start, end])
          }
        }]
      },
      statusMap,
      orderDialogVisible: false,
      orderDetail: {
        order: {},
        user: {}
      },
      refundForm: {
        orderId: undefined,
        refundMoney: undefined
      },
      refundDialogVisible: false,
      downloadLoading: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    checkPermission,
    getList() {
      this.listLoading = true
      if (this.listQuery.timeArray && this.listQuery.timeArray.length === 2) {
        this.listQuery.start = this.listQuery.timeArray[0]
        this.listQuery.end = this.listQuery.timeArray[1]
      } else {
        this.listQuery.start = null
        this.listQuery.end = null
      }

      listOrder(this.listQuery).then(response => {
        this.list = response.data.data.list
        this.total = response.data.data.total
        this.listLoading = false
      }).catch(() => {
        this.list = []
        this.total = 0
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleDetail(row) {
      detailOrder(row.rideOrderId).then(response => {
        this.orderDetail = response.data.data
      })
      this.orderDialogVisible = true
    },
    handleDelete(row) {
      deleteOrder({ rideOrderId: row.rideOrderId }).then(response => {
        this.$notify.success({
          title: '成功',
          message: '订单删除成功'
        })
        this.getList()
      }).catch(response => {
        this.$notify.error({
          title: '失败',
          message: response.data.errmsg
        })
      })
    },
    handleRefund(row) {
      this.refundForm.orderId = row.rideOrderId
      this.refundForm.refundMoney = row.deposit

      this.refundDialogVisible = true
      this.$nextTick(() => {
        this.$refs['refundForm'].clearValidate()
      })
    },
    confirmRefund() {
      this.$refs['refundForm'].validate((valid) => {
        if (valid) {
          refundOrder(this.refundForm).then(response => {
            this.refundDialogVisible = false
            this.$notify.success({
              title: '成功',
              message: '确认退款成功'
            })
            this.getList()
          }).catch(response => {
            this.$notify.error({
              title: '失败',
              message: response.data.errmsg
            })
          })
        }
      })
    },
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['订单编号', '用户ID', '订单状态']
        const filterVal = ['rideOrderId', 'userId', 'status']
        excel.export_json_to_excel2(tHeader, this.list, filterVal, '网约车订单信息')
        this.downloadLoading = false
      })
    },
    printOrder() {
      this.$print(this.$refs.print)
      this.orderDialogVisible = false
    }
  }
}
</script>
