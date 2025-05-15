<template>
    <el-container class="app-container">
      <el-aside width="220px" class="sidebar">
        <div class="logo">
          <img src="" alt="Logo" class="logo-img" />
          <span class="logo-text">管理系统</span>
        </div>
        <el-menu
          :default-active="activeMenu"
          class="el-menu-vertical"
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
          @select="handleMenuSelect"
        >
          <el-menu-item index="1">
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
          <!-- <el-menu-item index="2">
            <el-icon><Files /></el-icon>
            <span>权限管理</span> -->
          <!-- </el-menu-item> -->
          <el-menu-item index="3">
            <el-icon><ShoppingCart /></el-icon>
            <span>游戏管理</span>
          </el-menu-item>
          <el-menu-item index="4">
            <el-icon><PieChart /></el-icon>
            <span>评论管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
  
      <el-container>
        <el-header class="app-header">
          <div class="header-left">
            <el-breadcrumb separator="/">
              <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
              <el-breadcrumb-item>{{ activeMenuTitle }}</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          <div class="header-right">
            <el-dropdown>
              <span class="el-dropdown-link">
                管理员 
                <!-- <el-icon class="el-icon--right"><arrow-down /></el-icon> -->
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>个人信息</el-dropdown-item>
                  <el-dropdown-item @click="logout()">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>
  
        <el-main>
          <component :is="activeComponent"></component>
        </el-main>
      </el-container>
    </el-container>
  </template>
  
<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { User, ShoppingCart, PieChart } from '@element-plus/icons-vue'
import UserManagement from '@/components/UserManagement.vue';
import GameManagement from '@/components/GameManagement.vue';
import Cookies from 'js-cookie'
import router from "@/router";
import CommentManagement from '@/components/CommentManagement.vue';

const logout = () => {
  Cookies.remove('user');
  router.push({ name: "LoginRegister" });
};
  const activeMenu = ref('1')
  const activeComponent = computed(() => {
    switch (activeMenu.value) {
    //   case '1': return UserManagement
    //   case '2': return PermissionManagement
      case '3': return GameManagement
      case '4': return CommentManagement
      default: return UserManagement
    }
  })
  
  const activeMenuTitle = computed(() => {
    switch (activeMenu.value) {
      case '1': return '用户管理'
      case '2': return '权限管理'
      case '3': return '商品管理'
      case '4': return '数据统计'
      default: return '用户管理'
    }
  })
  
  const handleMenuSelect = (index: string) => {
    activeMenu.value = index
  }
  onMounted(() => {
  const user = Cookies.get('user');
  if (!user) {
    router.push('/');
  }
});
</script>
  
<style scoped>
  .app-container {
    height: 100vh;
  }
  
  .sidebar {
    background-color: #304156;
    color: #bfcbd9;
  }
  
  .logo {
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: #2b2f3a;
  }
  
  .logo-img {
    width: 40px;
    height: 40px;
    margin-right: 10px;
  }
  
  .logo-text {
    font-size: 20px;
    font-weight: bold;
    color: #fff;
  }
  
  .el-menu-vertical {
    border-right: none;
  }
  
  .app-header {
    background-color: #fff;
    color: #333;
    line-height: 60px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 20px;
    box-shadow: 0 1px 4px rgba(0,21,41,.08);
  }
  
  .header-left {
    display: flex;
    align-items: center;
  }
  
  .header-right {
    display: flex;
    align-items: center;
  }
  
  .el-dropdown-link {
    cursor: pointer;
    color: #409EFF;
  }
  
  .el-main {
    background-color: #f0f2f5;
    padding: 20px;
  }
</style>