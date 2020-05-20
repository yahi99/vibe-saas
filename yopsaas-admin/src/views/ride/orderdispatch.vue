<template>
  <div class="app-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input v-model="listQuery.rideOrderId" clearable class="filter-item" style="width: 160px;" placeholder="请输入订单ID" />
      <el-input v-model="listQuery.rideOrderDispatchId" clearable class="filter-item" style="width: 160px;" placeholder="请输入派单编号" />
      <el-date-picker v-model="listQuery.timeArray" type="datetimerange" class="filter-item" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" :picker-options="pickerOptions" />
      <el-select v-model="listQuery.rideOrderStatusArray" multiple style="width: 200px" class="filter-item" placeholder="请选择决策状态">
        <el-option v-for="(key, value) in statusMap" :key="key" :label="key" :value="value" />
      </el-select>
      <el-button v-permission="['GET /admin/rideorderdispatch/list']" class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">导出</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table v-loading="listLoading" :data="list" element-loading-text="正在查询中。。。" border fit highlight-current-row>

      <el-table-column align="center" min-width="100" label="派单编号" prop="rideOrderDispatchId" />

      <el-table-column align="center" label="订单ID" prop="rideOrderId" />

      <el-table-column align="center" label="司机ID" prop="driverId" />
      <el-table-column align="center" label="司机姓名" prop="name" />
      <el-table-column align="center" label="星级" prop="score" />
      <el-table-column align="center" label="好评率" prop="goodCommentRate" />
      <el-table-column align="center" label="车辆品牌" prop="brand" />
      <el-table-column align="center" label="车型" prop="carType" />

      <el-table-column align="center" label="时间" prop="createTime">
        <template slot-scope="scope">
          {{ scope.row.createTime | timeFilter }}
        </template>
      </el-table-column>

      <el-table-column align="center" label="决策状态" prop="status">
        <template slot-scope="scope">
          {{ scope.row.status | statusFilter }}
        </template>
      </el-table-column>

      <el-table-column align="center" label="操作" width="160" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-permission="['GET /admin/rideorderdispatch/detail']" type="primary" size="mini" @click="handleDetail(scope.row)">详情</el-button>
          <!--<el-button v-permission="['POST /admin/rideorderdispatch/delete']" type="danger" size="mini" @click="handleDelete(scope.row)">删除</el-button>-->
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <!-- 派单详情对话框 -->
    <el-dialog :visible.sync="orderDialogVisible" title="派单详情" width="800">
      <section ref="print">
        <el-form :data="dispatchDetail" label-position="left">
          <el-form-item label="派单编号">
            <span>{{ dispatchDetail.orderDispatch.rideOrderDispatchId }}</span>
          </el-form-item>
          <el-form-item label="决策状态">
            <el-tag>{{ dispatchDetail.orderDispatch.status | statusFilter }}</el-tag>
          </el-form-item>
          <el-form-item label="司机姓名">
            <span>{{ dispatchDetail.orderDispatch.name }}</span>
          </el-form-item>
          <el-form-item label="星级">
            <span>{{ dispatchDetail.orderDispatch.score }}</span>
          </el-form-item>
          <el-form-item label="派单信息">
            <span>（车辆品牌）{{ dispatchDetail.orderDispatch.brand }}</span>
            <span>（车型）{{ dispatchDetail.orderDispatch.carType }}</span>
          </el-form-item>
        </el-form>
      </section>
      <span slot="footer" class="dialog-footer">
        <el-button @click="orderDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="printOrder">打 印</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { detailDispatch, listDispatch, deleteDispatch } from '@/api/rideorderdispatch'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import checkPermission from '@/utils/permission' // 权限判断函数

const statusMap = {
  0: '未选',
  1: '已选'
}

export default {
  name: 'RideOrder',
  components: { Pagination },
  filters: {
    statusFilter(status) {
      return statusMap[status]
    },
    timeFilter(time) {
      if (time === 0) {
        return ''
      }
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
        sort: 'ride_order_dispatch_id',
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
      dispatchDetail: {
        orderDispatch: {}
      },
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

      listDispatch(this.listQuery).then(response => {
        this.list = response.data.data.list
        for (const i in this.list) {
          this.list[i]['statusTxt'] = statusMap[this.list[i]['status']]
        }
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
      detailDispatch(row.rideOrderDispatchId).then(response => {
        this.dispatchDetail = response.data.data
      })
      this.orderDialogVisible = true
    },
    handleDelete(row) {
      deleteDispatch({ rideOrderDispatchId: row.rideOrderDispatchId }).then(response => {
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
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['派单编号', '订单ID', '司机ID', '司机姓名', '星级', '好评率', '车辆品牌', '车型', '决策状态']
        const filterVal = ['rideOrderDispatchId', 'rideOrderId', 'driverId', 'name', 'score', 'goodCommentRate', 'brand', 'carType', 'statusTxt']
        excel.export_json_to_excel2(tHeader, this.list, filterVal, '网约车派单信息')
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
