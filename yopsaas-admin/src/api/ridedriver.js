import request from '@/utils/request'
import Qs from 'qs'

export function listDriver(query) {
  return request({
    url: '/ridedriver/list',
    method: 'get',
    params: query,
    paramsSerializer: function(params) {
      return Qs.stringify(params, { arrayFormat: 'repeat' })
    }
  })
}

export function detailDriver(ycDriverId) {
  return request({
    url: '/ridedriver/detail',
    method: 'get',
    params: { ycDriverId }
  })
}
