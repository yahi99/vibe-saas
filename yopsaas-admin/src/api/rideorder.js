import request from '@/utils/request'
import Qs from 'qs'

export function listOrder(query) {
  return request({
    url: '/rideorder/list',
    method: 'get',
    params: query,
    paramsSerializer: function(params) {
      return Qs.stringify(params, { arrayFormat: 'repeat' })
    }
  })
}

export function detailOrder(id) {
  return request({
    url: '/rideorder/detail',
    method: 'get',
    params: { id }
  })
}

export function refundOrder(data) {
  return request({
    url: '/rideorder/refund',
    method: 'post',
    data
  })
}

export function deleteOrder(data) {
  return request({
    url: '/rideorder/delete',
    method: 'post',
    data
  })
}

export function replyComment(data) {
  return request({
    url: '/rideorder/reply',
    method: 'post',
    data
  })
}

export function listChannel(id) {
  return request({
    url: '/rideorder/channel',
    method: 'get'
  })
}
