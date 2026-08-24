import request from '@/utils/request'

// 分页查询逾期记录
export function queryOverdueByPage(params) {
    return request({
        url: '/borrow/overdue',
        method: 'get',
        params
    })
}

// 查询当前逾期中的记录
export function queryCurrentOverdue() {
    return request({
        url: '/borrow/overdue/current',
        method: 'get'
    })
}

// 获取逾期统计数据
export function getOverdueStatistics() {
    return request({
        url: '/borrow/statistics/overdue',
        method: 'get'
    })
}