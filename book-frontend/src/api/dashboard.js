import request from '@/utils/request'
import { queryBookTypes } from '@/api/booktype'
import { queryBookInfosByPage, queryBookInfos } from '@/api/bookinfo'
import { queryBorrowsByPage } from '@/api/borrow'

// 获取图书类型分布数据
export async function getBookTypeDistribution() {
  try {
    // 获取所有图书类型
    const types = await queryBookTypes()
    
    // 处理每种类型的图书数量
    const result = await Promise.all(types.map(async (type) => {
      const params = {
        page: 1,
        limit: 1,
        booktypeid: type.booktypeid
      }
      
      const response = await queryBookInfosByPage(params)
      return {
        name: type.booktypename,
        value: response.count || 0
      }
    }))
    
    return result
  } catch (error) {
    console.error('获取图书类型分布数据失败:', error)
    return []
  }
}

// 获取借阅状态分布数据（替代原有的借阅量统计）
export async function getBorrowStatusDistribution() {
  try {
    // 获取所有图书信息
    const books = await queryBookInfos()
    
    // 统计借阅状态分布
    let borrowedCount = 0
    let availableCount = 0
    
    books.forEach(book => {
      if (book.isborrowed === 1) {
        borrowedCount++
      } else {
        availableCount++
      }
    })
    
    return {
      categories: ['在馆图书', '借出图书'],
      values: [availableCount, borrowedCount]
    }
  } catch (error) {
    console.error('获取借阅状态分布数据失败:', error)
    return {
      categories: [],
      values: []
    }
  }
}

// 获取高价图书排行榜（替代热门图书排行）
export async function getHighPriceBooks() {
  try {
    // 获取所有图书
    const books = await queryBookInfos()
    
    // 按价格排序图书
    const pricedBooks = books
      .filter(book => book.bookname && book.bookprice)
      .map(book => ({
        name: book.bookname,
        value: parseFloat(book.bookprice) || 0
      }))
      .sort((a, b) => b.value - a.value)
      .slice(0, 6)
    
    return {
      books: pricedBooks.map(book => book.name),
      values: pricedBooks.map(book => book.value)
    }
  } catch (error) {
    console.error('获取图书价格统计数据失败:', error)
    return {
      books: [],
      values: []
    }
  }
} 