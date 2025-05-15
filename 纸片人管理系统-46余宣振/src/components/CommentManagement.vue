<template>
    <div class="comment-management">
      <h3>评论管理</h3>
      <div class="search-comments">
        <el-input
          v-model="searchQuery"
          placeholder="搜索评论内容、用户名或游戏名"
          class="search-input"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-select v-model="searchType" placeholder="选择搜索类型">
          <el-option label="评论内容" value="content" />
          <el-option label="用户名" value="username" />
          <el-option label="游戏名" value="gameName" />
        </el-select>
        <el-button type="primary" @click="searchComments">搜索</el-button>
      </div>
      <el-table :data="filteredComments" style="width: 100%">
        <el-table-column prop="username" label="用户" width="120" />
        <el-table-column prop="title" label="标题" width="180" />
        <el-table-column prop="content" label="内容" />
        <el-table-column label="时间" width="180">
          <template #default="{ row }">
            {{ formatTimestamp(row.timestamp) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button
              type="danger"
              size="small"
              @click="deleteComment(row.id)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref, onMounted, computed } from 'vue'
  import { ElMessage } from 'element-plus'
  import { Search } from '@element-plus/icons-vue'
  import axios from 'axios'
  
  interface Comment {
    id: number
    username: string
    content: string
    timestamp: string
    title: string
  }

  
  const comments = ref<Comment[]>([])
  const searchQuery = ref('')
  const searchType = ref('content')
  
  const fetchComments = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/comment/selectAll`)
      comments.value = response.data
    } catch (error) {
      console.error('获取评论失败:', error)
      ElMessage.error('获取评论失败，请稍后再试')
    }
  }
  
  const deleteComment = async (commentId: number) => {
    try {
      const response = await axios.delete(`http://localhost:8080/comment/delete?id=${commentId}`)
      if (response.data) {
        ElMessage.success('评论删除成功')
        await fetchComments()
      } else {
        ElMessage.error('评论删除失败')
      }
    } catch (error) {
      console.error('删除评论失败:', error)
      ElMessage.error('删除评论失败，请稍后再试')
    }
  }
  
  const formatTimestamp = (timestamp: string) => {
    const date = new Date(timestamp)
    return date.toLocaleString()
  }
  
  const searchComments = () => {
    // 搜索功能已通过 computed 属性 filteredComments 实现
  }
  
  const filteredComments = computed(() => {
    if (!searchQuery.value) return comments.value
    const query = searchQuery.value.toLowerCase()
    return comments.value.filter(comment => {
      switch (searchType.value) {
        case 'content':
          return comment.content.toLowerCase().includes(query) || comment.title.toLowerCase().includes(query)
        case 'username':
          return comment.username.toLowerCase().includes(query)
        case 'gameName':
          return comment.title.toLowerCase().includes(query)
        default:
          return true
      }
    })
  })
  
  onMounted(() => {
    fetchComments()
  })
  </script>
  
  <style scoped>
  .comment-management {
    margin-top: 20px;
  }
  
  .search-comments {
    display: flex;
    gap: 10px;
    margin-bottom: 20px;
  }
  
  .search-input {
    flex-grow: 1;
  }
  </style>
  
  