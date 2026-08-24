import request from '@/utils/request'

// 缴费
export function pay(data) {
    return request({
        url: '/payment/pay',
        method: 'post',
        data
    })
}

// 查询某读者的缴费记录
export function getPaymentsByUserId(userId) {
    return request({
        url: '/payment/list',
        method: 'get',
        params: {
            userId
        }
    })
}

// 分页查询所有缴费记录
export function queryPaymentsByPage(params) {
    return request({
        url: '/payment/queryPaymentsByPage',
        method: 'get',
        params
    })
}

// 查询某读者未缴费的逾期记录
export function getUnpaidOverdue(userId) {
    return request({
        url: '/payment/unpaid',
        method: 'get',
        params: {
            userId
        }
    })
}