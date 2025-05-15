<template>
    <div class="user-management">
      <h1>用户管理</h1>
      <div class="controls">
        <el-input
          v-model="searchQuery"
          placeholder="搜索用户"
          class="search-input"
          @input="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-button type="primary" @click="showCreateUserDialog">
          新建用户
        </el-button>
      </div>
  
      <el-table :data="filteredUsers" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" width="180" />
        <el-table-column prop="password" label="密码" width="180">
          <template #default="{ row }">
        <span v-if="row.showPassword">{{ row.password }}</span>
        <span v-else>••••••••</span>
        <el-button link type="primary" @click="togglePassword(row)">
          {{ row.showPassword ? '隐藏' : '显示' }}
        </el-button>
      </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button type="primary" @click="editUser(row)">修改</el-button>
            <el-button type="danger" @click="deleteUser(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
  
      <el-dialog v-model="dialogVisible" :title="dialogTitle">
        <el-form :model="currentUser" label-width="100px">
          <el-form-item label="用户名">
            <el-input v-model="currentUser.username" />
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="currentUser.password" type="password" />
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary"  @click="dialogTitle === '新建用户' ? createUser() : alterUser()" > 
              确定
            </el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref, computed, onMounted } from 'vue'
  import { ElMessage, ElMessageBox } from 'element-plus'
  import { Search } from '@element-plus/icons-vue'
import axios from 'axios';
  
  interface User {
    id: number
    username: string
    password: string
    showPassword: boolean

  }
  onMounted(()=>{
    fetchGames();
  })
  const users = ref<User[]>([]);
    const fetchGames = async () => {
        try {
            const response = await axios.get('http://localhost:8080/user/all');  // 假设你的后端接口是这个
            users.value = response.data;  // 将返回的数据赋值给games
        } catch (error) {
            console.error("获取游戏数据失败:", error);
        }
    };
  
  const searchQuery = ref('')
  const dialogVisible = ref(false)
  const dialogTitle = ref('')
  const currentUser = ref<User>({ id: 0, username: '', password: '' , showPassword: false })
  
  const filteredUsers = computed(() => {
    if (!searchQuery.value) return users.value
    return users.value.filter(user => 
      user.username.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      user.id.toString().includes(searchQuery.value)
    )
  })
  
  const handleSearch = () => {
    // The filtering is handled by the computed property
  }
  const togglePassword = (user: User) => {
  user.showPassword = !user.showPassword;
};
  
  const showCreateUserDialog = () => {
    currentUser.value = { id: 0, username: '', password: '' , showPassword: false }
    dialogTitle.value = '新建用户'
    dialogVisible.value = true
    
  }
  
  const editUser = (user: User) => {
    currentUser.value = { ...user }
    dialogTitle.value = '修改用户'
    dialogVisible.value = true
  }
  const deleteUser=(user:User)=>[
  currentUser.value = { ...user },
  deleteUser2()
  ]
  const createUser = async () => {
  // 确保用户名和密码不为空
  if (!currentUser.value.username || !currentUser.value.password) {
    alert('用户名和密码不能为空');
    return;
  }

  try {
    // 发送请求到后端注册新用户
    const res = await axios.get("http://localhost:8080/user/register", {
      params: {
        username: currentUser.value.username,
        password: currentUser.value.password
      }
    });

    // 处理返回的结果
    if (res.data) {
      alert('用户创建成功');
      dialogVisible.value = false;  // 关闭对话框
      fetchGames();
    } else {
      alert('用户创建失败 ');
    }
  } catch (error) {
    console.error('创建用户失败:', error);
    alert('创建用户失败，请稍后再试');
  }
};
  const alterUser=async()=>{
    if (!currentUser.value.username || !currentUser.value.password) {
    alert('用户名和密码不能为空');
    return;
  }
  
  try {
    // 发送请求到后端注册新用户
    const res = await axios.get("http://localhost:8080/user/alter", {
      params: {
        id:currentUser.value.id,
        username: currentUser.value.username,
        password: currentUser.value.password
      }
    });
    alert(res.data)

    // 处理返回的结果
    if (res.data.success) {
      alert('用户修改成功');
      dialogVisible.value = false;  // 关闭对话框
      fetchGames()
    } else {
      alert('用户修改失败: ');
    }
  } catch (error) {
    alert('用户名重复');
  }

  }
  const deleteUser2=async()=>{
    const res = await axios.get("http://localhost:8080/user/delete",{
      params:{
        id:currentUser.value.id
      }
    })
    if (res.data) {
      alert('用户删除成功');
      dialogVisible.value = false;  // 关闭对话框
      fetchGames()
    } else {
      alert('用户删除失败 ');
    }
  }

  
  const showPassword = (user: User) => {
    ElMessageBox.alert(user.password, '密码', {
      confirmButtonText: '确定',
    })
  }
  </script>
  
  <style scoped>
  .user-management {
    padding: 20px;
  }
  
  .controls {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;
  }
  
  .search-input {
    width: 300px;
  }
  
  .el-table {
    margin-top: 20px;
  }
  </style>