import request from '@/utils/request'
import Qs from 'qs'

export function listDispatch(query) {
  return request({
    url: '/rideorderdispatch/list',
    method: 'get',
    params: query,
    paramsSerializer: function(params) {
      return Qs.stringify(params, { arrayFormat: 'repeat' })
    }
  })
}

export function detailDispatch(rideOrderDispatchId) {
  return request({
    url: '/rideorderdispatch/detail',
    method: 'get',
    params: { rideOrderDispatchId }
  })
}

export function deleteDispatch(data) {
  return request({
    url: '/rideorderdispatch/delete',
    method: 'post',
    data
  })
}
