<template>
  <div class="app-container">
    <el-form ref="dataForm" :rules="rules" :model="dataForm" status-icon label-width="300px">
      <el-form-item label="订单派单中未处理等待系统取消" prop="yopsaas_ride_order_unchoose_car">
        <el-input v-model="dataForm.yopsaas_ride_order_unchoose_car" class="input-width">
          <template slot="append">分钟</template>
        </el-input>
        <span class="info">订单派单中未处理，则订单自动取消</span>
      </el-form-item>
      <el-form-item>
        <el-button @click="cancel">取消</el-button>
        <el-button type="primary" @click="update">确定</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { listYopOrder, updateYopOrder } from '@/api/config'

export default {
  name: 'ConfigYopOrder',
  data() {
    return {
      dataForm: {
        yopsaas_ride_order_unchoose_car: 0
      },
      rules: {
        yopsaas_ride_order_unchoose_car: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.init()
  },
  methods: {
    init: function() {
      listYopOrder().then(response => {
        this.dataForm = response.data.data
      })
    },
    cancel() {
      this.init()
    },
    update() {
      this.$refs['dataForm'].validate((valid) => {
        if (!valid) {
          return false
        }
        this.doUpdate()
      })
    },
    doUpdate() {
      updateYopOrder(this.dataForm)
        .then(response => {
          this.$notify.success({
            title: '成功',
            message: '网约车订单参数配置成功'
          })
        })
        .catch(response => {
          this.$notify.error({
            title: '失败',
            message: response.data.errmsg
          })
        })
    }
  }
}
</script>
<style scoped>
  .input-width {
    width: 50%;
  }
  .info {
    margin-left: 15px;
  }
</style>
