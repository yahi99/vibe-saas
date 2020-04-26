<template>
  <div class="app-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input v-model="listQuery.ycDriverId" clearable class="filter-item" style="width: 160px;" placeholder="请输入第三方司机ID" />
      <el-input v-model="listQuery.cellphone" clearable class="filter-item" style="width: 160px;" placeholder="请输入手机号" />
      <el-date-picker v-model="listQuery.timeArray" type="datetimerange" class="filter-item" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" :picker-options="pickerOptions" />
      <el-button v-permission="['GET /admin/ridedriver/list']" class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">导出</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table v-loading="listLoading" :data="list" element-loading-text="正在查询中。。。" border fit highlight-current-row>

      <el-table-column align="center" min-width="100" label="司机ID" prop="rideDriverId" />

      <el-table-column align="center" label="第三方司机ID" prop="ycDriverId" />
      <el-table-column align="center" label="司机姓名" prop="name" />
      <el-table-column align="center" label="司机电话" prop="cellphone" />
      <el-table-column align="center" label="星级" prop="starLevel" />
      <el-table-column align="center" label="月完成订单数" prop="unittimeCompleteCount" />
      <el-table-column align="center" label="车辆品牌" prop="brand" />
      <el-table-column align="center" label="车型" prop="carType" />

      <el-table-column align="center" label="时间" prop="createTime">
        <template slot-scope="scope">
          {{ scope.row.createTime | timeFilter }}
        </template>
      </el-table-column>

      <el-table-column align="center" label="操作" width="160" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-permission="['GET /admin/ridedriver/detail']" type="primary" size="mini" @click="handleDetail(scope.row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <!-- 派单详情对话框 -->
    <el-dialog :visible.sync="driverDialogVisible" title="司机详情" width="800">
      <section ref="print">
        <el-form :data="driverDetail" label-position="left">
          <el-form-item label="司机ID">
            <span>{{ driverDetail.driver.rideDriverId }}</span>
          </el-form-item>
          <el-form-item label="第三方司机ID">
            <span>{{ driverDetail.driver.ycDriverId }}</span>
          </el-form-item>
          <el-form-item label="司机姓名">
            <span>{{ driverDetail.driver.name }}</span>
          </el-form-item>
          <el-form-item label="星级">
            <span>{{ driverDetail.driver.starLevel }}</span>
          </el-form-item>
          <el-form-item label="派单信息">
            <span>（车辆品牌）{{ driverDetail.driver.brand }}</span>
            <span>（车型）{{ driverDetail.driver.carType }}</span>
          </el-form-item>
        </el-form>
      </section>
      <span slot="footer" class="dialog-footer">
        <el-button @click="driverDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="printDriver">打 印</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { listDriver, detailDriver } from '@/api/ridedriver'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import checkPermission from '@/utils/permission' // 权限判断函数

export default {
  name: 'RideDriver',
  components: { Pagination },
  filters: {
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
        ycDriverId: undefined,
        timeArray: [],
        sort: 'ride_driver_id',
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
      driverDialogVisible: false,
      driverDetail: {
        driver: {}
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

      listDriver(this.listQuery).then(response => {
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
      detailDriver(row.ycDriverId).then(response => {
        this.driverDetail = response.data.data
      })
      this.driverDialogVisible = true
    },
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['司机ID', '司机姓名', '司机电话']
        const filterVal = ['rideDriverId', 'name', 'cellphone']
        excel.export_json_to_excel2(tHeader, this.list, filterVal, '网约车司机信息')
        this.downloadLoading = false
      })
    },
    printDriver() {
      this.$print(this.$refs.print)
      this.driverDialogVisible = false
    }
  }
}
</script>
