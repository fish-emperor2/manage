<template>
  <div class="main-container">
    <ParticleBackground />
    <nav class="nav-bar">
      <div class="brand">纸片人百科</div>
      <div class="nav-items">
        <a href="/" class="nav-item" >首页</a>
      </div>
    </nav>
    <div class="back-button">
      <el-button type="primary" @click="goBack">返回上一级</el-button>
    </div>
    <div class="content">
      <div class="tags">  
  <span class="tag" v-for="tag in gameData?.tags" :key="tag">{{ tag }}</span>  
</div>  

<h1 class="title">{{ gameData?.title }}</h1>  
<h2 class="subtitle">{{ gameData?.subtitle }}</h2>  

<div class="game-card">  
  <img :src="gameData?.imageUrl" alt="Game Preview" class="game-image">  
  <div class="game-details">  
    <div class="detail-item">  
      <span class="label">品牌:</span>  
      <span class="value">{{ gameData?.brand }}</span>  
    </div>  
    <div class="detail-item">  
      <span class="label">发售日期:</span>  
      <span class="value">{{formatTimestamp(gameData?.releaseDate)  }}</span>  
    </div>  
    <div class="detail-item">  
      <span class="label">类型:</span>  
      <span class="value">{{ gameData?.gameType }}</span>  
    </div>  
  </div>  
  <div class="story-section">  
    <h2>  
      <el-icon><Document /></el-icon>  
      故事简介  
    </h2>  
    <div class="story-content">  
      <pre style="color:white; font-family:微软雅黑;white-space: pre-wrap; word-wrap: break-word;" v-html="gameData?.storyIntro"></pre>  
    </div>  
  </div>  
  <div class="screenshots-section">
          <h2>
            <el-icon><Picture /></el-icon>
            游戏截图
          </h2>
          <el-row :gutter="20" class="screenshot-grid">
            <el-col :span="6" v-for="(img, index) in gameImage" :key="index">
  <el-image 
    :src="img.imageUrl"
    fit="cover"
    :preview-src-list="[img.imageUrl]" 
  />
</el-col>
          </el-row>
  </div>     
<div class="comments-section">  
  <div class="comments-header">  
    <h3 class="comments-title">  
      <el-icon><ChatLineRound /></el-icon>  
      评论  
    </h3>  
  </div>  
  <div class="comment-form">  
    <div class="form-content">  
      <el-input  
        type="textarea"  
        v-model="commentContent"  
        :rows="4"  
        placeholder="欢迎评论"  
      />  
      <div class="form-toolbar">  
        <div class="toolbar-left">  
          <button class="toolbar-btn">  
            <el-icon><Document /></el-icon>  
          </button>  
          <button class="toolbar-btn">  
            <el-icon><Picture /></el-icon>  
          </button>  
          <button class="toolbar-btn">GIF</button>  
          <button class="toolbar-btn">  
            <el-icon><PictureFilled /></el-icon>  
          </button>  
        </div>  
        <div class="toolbar-right">  
          <span class="word-count">{{ commentContent ? commentContent.length : 0 }} 字</span>  
          <!-- <el-button type="primary" class="login-btn" >登录</el-button>   -->
          <el-button type="success" class="submit-btn" @click="submitComment()">发表</el-button>  
        </div>  
      </div>  
    </div>  
  </div>  
  <div v-if="comments.length > 0">
  <div v-for="comment in comments" :key="comment.id" class="comment-item">
    <p><strong>用户:</strong> {{ comment.username }}</p>
    <p>{{ comment.content }}</p>
    <p><small>{{ formatTimestamp(comment.timestamp) }}</small></p>
  </div>
</div>
  
  <div v-else class="no-comments">  
    <p>来发评论吧~</p>  
  </div>  
</div>  
      </div>
    </div>
    <canvas ref="canvas" class="particle-canvas"></canvas>
  </div>
</template>
<script setup lang="ts">  
import { onMounted, ref } from 'vue';  

import { useRoute ,useRouter} from 'vue-router';
import axios from 'axios';
import Cookies from 'js-cookie';
import ParticleBackground from '@/components/ParticleBackground.vue';

const router = useRouter(); // 使用 useRouter

const goBack = () => {
  router.go(-1); // 返回上一级
};
const gameImage = ref<any[]>([]);
const comments = ref<any[]>([]);
const commentContent = ref('');  
interface GameData {
  id: number;
  title: string;
  subtitle: string;
  brand: string;
  releaseDate: string;
  gameType: string;
  imageUrl: string;
  createdAt: string;
  updatedAt: string;
  heart: number;
  storyIntro: string;
  tags: string[];
}
const submitComment = async () => {
  if (!commentContent.value.trim()) {
    alert('评论内容不能为空');
    return;
  }
  const user = Cookies.get('user');
  if (!user) {
    alert('请先登录');
    localStorage.setItem('savedComment', commentContent.value);
    router.push({ name: 'LoginRegister', query: { redirect: router.currentRoute.value.fullPath } });
    return;
  }
  const { id, username } = JSON.parse(user);
  const newComment = {
    userId: id,  
    gameId: gameData.value?.id,
    content: commentContent.value,
  };
  try {
    await axios.post('http://localhost:8080/comment/add', newComment);
    commentContent.value = '';
    await fetch();
  } catch (error) {
    console.error('提交评论失败:', error);
    alert('评论提交失败，请稍后重试');
  }
};
const route = useRoute()
const gameData = ref<GameData | null>(null)
async function fetch(){
  const gameDataString = route.query.gameData as string
  if (gameDataString) {
    gameData.value = JSON.parse(gameDataString)
    console.log(gameData.value?.id)
    const res=await axios.get(`http://localhost:8080/screenshot/selectByid?id=${gameData.value?.id}`)
    gameImage.value=res.data;
    const response=await axios.get(`http://localhost:8080/comment/selectByid?id=${gameData.value?.id}`)
    comments.value=response.data;
  } else {
    console.log('没有接收到游戏数据')
  }
}
onMounted(async () => {
  const savedComment = localStorage.getItem('savedComment');
  if (savedComment) {
    commentContent.value = savedComment;
    localStorage.removeItem('savedComment');
  }
 fetch();
})
const formatTimestamp = (timestamp: string) => {
  const date = new Date(timestamp);
  return date.toLocaleString();
};
</script>


<style scoped>
.story-pre {
    color: white;
    font-family: 微软雅黑;
    white-space: pre-wrap; /* 换行处理 */
    word-wrap: break-word; /* 长单词换行处理 */
}
.back-button {
  margin: 1rem 2rem;
}
.story-content {
  white-space: pre-wrap;

}
.story-content :deep(p) {
  margin: 0;
  line-height: 1.8;
}

.main-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 100%);
  color: white;
  position: relative;
  overflow: hidden;
}

.particle-canvas {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
}

.nav-bar {
  position: relative;
  z-index: 2;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 2rem;
  /* background: rgba(0, 0, 0, 0.3); */
}

.brand {
  font-size: 1.5rem;
  font-weight: bold;
}

.nav-items {
  display: flex;
  gap: 2rem;
}

.nav-item {
  color: white;
  text-decoration: none;
  opacity: 0.8;
  transition: opacity 0.3s;
}

.nav-item:hover {
  opacity: 1;
}

.content {
  position: relative;
  z-index: 2;
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-bottom: 2rem;
}

.tag {
  background: rgba(255, 255, 255, 0.1);
  padding: 0.25rem 0.75rem;
  border-radius: 1rem;
  font-size: 0.875rem;
}

.title {
  font-size: 2rem;
  margin-bottom: 0.5rem;
  font-weight: bold;
}

.subtitle {
  font-size: 1.5rem;
  margin-bottom: 2rem;
  opacity: 0.8;
}

.game-card {
  background: rgba(0, 0, 0, 0.3);
  border-radius: 1rem;
  padding: 2rem;
  /* backdrop-filter: blur(10px); */
}

.game-image {
  width: 100%;
  max-height: 450px;
  border-radius: 0.5rem;
  margin-bottom: 1.5rem;
  border-radius: 12px;
    object-fit: cover;
    display: block;
    margin: 0.8rem auto;
    max-width: 90%;
    max-height: 450px;
}

.game-details {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.detail-item {
  display: flex;
  gap: 1rem;
}

.label {
  opacity: 0.7;
  min-width: 80px;
}

@media (max-width: 768px) {
  .nav-bar {
    flex-direction: column;
    gap: 1rem;
    text-align: center;
  }

  .nav-items {
    flex-wrap: wrap;
    justify-content: center;
  }

  .title {
    font-size: 1.5rem;
  }

  .subtitle {
    font-size: 1.25rem;
  }
}
.comment-item {  
  background: rgba(255, 255, 255, 0.1);  
  border-radius: 8px;  
  padding: 1rem;  
  margin-bottom: 1rem;  
}  
.comments-section {
  margin-top: 2rem;
  background: rgba(0, 0, 0, 0.2);
  border-radius: 8px;
  padding: 1.5rem;
}

.comments-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.comments-title {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 1.25rem;
  margin: 0;
}

.comments-sort {
  display: flex;
  gap: 1rem;
}

.sort-item {
  color: #666;
  cursor: pointer;
  font-size: 0.875rem;
}

.sort-item.active {
  color: #42b883;
}

.comment-form {
  background: rgba(0, 0, 0, 0.2);
  border-radius: 8px;
  padding: 1rem;
}

.form-header {
  display: flex;
  gap: 1rem;
  margin-bottom: 1rem;
}

.header-input {
  max-width: 200px;
}

.form-content {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.form-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.5rem 0;
}

.toolbar-left {
  display: flex;
  gap: 0.5rem;
}

.toolbar-btn {
  background: transparent;
  border: none;
  color: #666;
  padding: 0.5rem;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

.toolbar-btn:hover {
  background: rgba(255, 255, 255, 0.1);
}

.toolbar-right {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.word-count {
  color: #666;
  font-size: 0.875rem;
}

.login-btn {
  background: transparent;
  border: 1px solid #42b883;
  color: #42b883;
}

.submit-btn {
  background: #42b883;
  border: none;
}

.no-comments {
  text-align: center;
  color: #666;
  padding: 2rem 0;
}

.powered-by {
  text-align: right;
  color: #666;
  font-size: 0.75rem;
  margin-top: 1rem;
}

.powered-by a {
  color: #42b883;
  text-decoration: none;
}

:deep(.el-input__inner) {
  background: rgba(0, 0, 0, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.1);
  color: white;
}

:deep(.el-textarea__inner) {
  background: rgba(0, 0, 0, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.1);
  color: white;
  resize: none;
}

:deep(.el-input__inner:focus),
:deep(.el-textarea__inner:focus) {
  border-color: #42b883;
}
</style>