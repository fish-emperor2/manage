<template>
  <ParticleBackground></ParticleBackground>
  <div class="min-h-screen flex items-center justify-center bg-gradient relative overflow-hidden" style="height: 100vh;">
    <div class="floating-bubbles"></div>
    
    <div class="card-container">
      <div class="card" :class="{ 'flipped': isLogin }">
        <!-- Register Side -->
        <div class="card-side front">
          <div class="card-content">
            <div class="form-side">
              <h2 class="text-2xl font-bold mb-8">注册</h2>
              <form @submit.prevent="handleRegister" class="space-y-4">
                <input 
                  v-model="registerForm.username" 
                  type="text" 
                  placeholder="用户名" 
                  class="input-field"
                />
                <input 
                  v-model="registerForm.password" 
                  type="password" 
                  placeholder="密码" 
                  class="input-field"
                />
                <input 
                  v-model="registerForm.confirmPassword" 
                  type="password" 
                  placeholder="确认密码" 
                  class="input-field"
                />
                <div class="flex items-center space-x-4">
                  <button type="submit" class="primary-button">注册</button>
                  <button 
                    type="button" 
                    class="text-blue-500 hover:text-blue-600"
                    @click="toggleForm"
                  >
                    已有账号?
                  </button>
                </div>
              </form>
            </div>
            <div class="welcome-side">
              <h1 class="text-3xl font-bold text-white mb-2">WELCOME</h1>
              <p class="text-white/80 mb-6">JOIN US</p>
              <img :src="avatarUrl" alt="Welcome" class="avatar" />
            </div>
          </div>
        </div>

        <!-- Login Side -->
        <div class="card-side back">
          <div class="card-content">
            <div class="welcome-side">
              <h1 class="text-3xl font-bold text-white mb-2">WELCOME</h1>
              <p class="text-white/80 mb-6">JOIN US</p>
              <img :src="avatarUrl" alt="Welcome" class="avatar" />
            </div>
            <div class="form-side">
              <h2 class="text-2xl font-bold mb-8">登录</h2>
              <form @submit.prevent="handleLogin" class="space-y-4">
                <input 
                  v-model="loginForm.username" 
                  type="text" 
                  placeholder="用户名" 
                  class="input-field"
                />
                <input 
                  v-model="loginForm.password" 
                  type="password" 
                  placeholder="密码" 
                  class="input-field"
                />
                <div class="flex items-center space-x-4">
                  <button type="submit" class="primary-button">登录</button>
                  <button 
                    type="button" 
                    class="text-sm text-gray-500 hover:text-gray-700"
                  >
                    忘记密码?
                  </button>
                </div>
                <button 
                  type="button" 
                  class="text-blue-500 hover:text-blue-600"
                  @click="toggleForm"
                >
                  没有账号?
                </button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import ParticleBackground from '@/components/ParticleBackground.vue';
import axios from 'axios';
import { useRouter } from "vue-router";
import Cookies from "js-cookie";

const router = useRouter();
const isLogin = ref(true)
const avatarUrl = '/placeholder.svg?height=120&width=120'

const loginForm = ref({
  username: '',
  password: ''
})

const registerForm = ref({
  username: '',
  password: '',
  confirmPassword: ''
})

const toggleForm = () => {
  isLogin.value = !isLogin.value
}

const handleLogin = async () => {
  try {
    let res = await axios.get("http://localhost:8080/user/login", {
      params: {
        username: loginForm.value.username,
        password: loginForm.value.password
      }
    });

    if (res.data) {
  Cookies.set("user", JSON.stringify({
    username: loginForm.value.username,
    id: res.data.id
  }));
  console.log(res.data.power);
  if (res.data.power === 1) {
    router.push({ name: "Test4" });
    return;
  } else {
    if (router.currentRoute.value.query.redirect) {
      const redirectPath = router.currentRoute.value.query.redirect;
      router.push(redirectPath);
    } else {
      router.push({
        name: "Test",
        query: { username: res.data.username }
      });
    }
  }
} else {
      alert("用户名或密码错误");
      handleReset();
    }
  } catch (error) {
    console.error("登录请求失败:", error);
    alert("服务器错误，请稍后重试！");
  }
};
const handleRegister = async () => {
  try {
    let res = await axios.get("http://localhost:8080/user/register", {
      params:{username: registerForm.value.username,
      password: registerForm.value.password}
    });
    if (res.data) {
      alert("注册成功，您可以登录了！");
      // 注册成功，切换到登录页面
      toggleForm()
    } else {
      alert("用户名已存在，请选择其他用户名！");
    }
  } catch (error) {
    console.error("注册请求失败:", error);
    alert("注册失败，请稍后重试！");
  }
};

const handleReset = () => {
  loginForm.value.username = "";
  loginForm.value.password = "";
};
</script>

<style scoped>
.min-h-screen {
  display: flex;
  justify-content: center;  /* 水平居中 */
  align-items: center;      /* 垂直居中 */
  height: 100vh;
  position: relative;
  overflow: hidden;
}
.bg-gradient {
  background: linear-gradient(120deg, #ffd1d1 0%, #c7e5ff 100%);
}

.floating-bubbles {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
  background-size: 50px 50px;
  animation: float 15s linear infinite;
}

.card-container {
  perspective: 1000px;
  width: 800px;
  height: 450px;
  display: flex;             /* 确保容器内内容排布 */
  justify-content: center;   /* 水平居中 */
  align-items: center;       /* 垂直居中 */
}

.card {
  position: relative;
  width: 100%;
  height: 100%;
  transition: transform 0.8s;
  transform-style: preserve-3d;
}

.card.flipped {
  transform: rotateY(180deg);
}

.card-side {
  position: absolute;
  width: 100%;
  height: 100%;
  backface-visibility: hidden;
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.back {
  transform: rotateY(180deg);
}

.card-content {
  display: flex;
  height: 100%;
}

.form-side, .welcome-side {
  flex: 1;
  padding: 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.welcome-side {
  background: linear-gradient(135deg, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0.2) 100%);
  border-radius: 20px;
  text-align: center;
}

.input-field {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid rgba(255, 255, 255, 0.3);
  background: rgba(255, 255, 255, 0.9);
  border-radius: 8px;
  transition: all 0.3s ease;
}

.input-field:focus {
  outline: none;
  border-color: rgba(59, 130, 246, 0.5);
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.25);
}

.primary-button {
  padding: 10px 24px;
  background: #3b82f6;
  color: white;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.primary-button:hover {
  background: #2563eb;
}

.avatar {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  margin: 0 auto;
  object-fit: cover;
  border: 4px solid rgba(255, 255, 255, 0.3);
}

@keyframes float {
  0% {
    background-position: 0 0;
  }
  100% {
    background-position: 50px 50px;
  }
}
</style>