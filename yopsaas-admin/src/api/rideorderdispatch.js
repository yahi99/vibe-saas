import request from '@/utils/request'
import Qs from 'qs'

export function listOrder(query) {
  return request({
    url: '/rideorderdispatch/list',
    method: 'get',
    params: query,
    paramsSerializer: function(params) {
      return Qs.stringify(params, { arrayFormat: 'repeat' })
    }
  })
}

export function detailOrder(rideOrderDispatchId) {
  return request({
    url: '/rideorderdispatch/detail',
    method: 'get',
    params: { rideOrderDispatchId }
  })
}

export function deleteOrder(data) {
  return request({
    url: '/rideorderdispatch/delete',
    method: 'post',
    data
  })
}
