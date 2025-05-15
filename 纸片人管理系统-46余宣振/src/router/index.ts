Home
Detail
Admin
import LoginRegister from '@/views/LoginRegister.vue'
import { createRouter, createWebHistory } from 'vue-router'
import CommentManagement from '@/components/CommentManagement.vue'
import Forum from '@/components/Forum.vue'
import Home from '@/views/home.vue'
import Detail from '@/views/detail.vue'
import Admin from '@/views/admin.vue'
LoginRegister
CommentManagement


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/test",
      name: "LoginRegister",
      component: LoginRegister,
    },
    {
      path: "/test3",
      name: "Test3",
      component: Detail,
    },
    {
      path: "/2",
      name: "Test4",
      component: Admin,
    },

    {
      path: "/",
      name: "Test",
      component: Home,
    },
    {
      path: "/forum",
      name: "Forum",
      component: Forum,
    },
  ]
})

export default router
