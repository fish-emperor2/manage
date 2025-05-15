<template>
  <div class="app-container">
    <ParticleBackground />
    <nav class="nav-bar">
    <div class="brand">纸片人百科 </div>
    <div class="nav-items">
      <a href="/" class="nav-item" >首页</a>
      <a href="#" class="nav-item" @click="handleAuth">{{ isLoggedIn ? '注销' : '登录' }}</a>    </div>
  </nav>
    <div class="category-bar">
      <el-tag effect="plain" class="active-tag" @click="selectTag('推荐')">
        推荐</el-tag>
      <el-tag effect="plain" class="active-tag" @click="selectTag('全部')">全部</el-tag>
      <el-icon v-if="currentPage > 1" class="arrow-icon" @click="prevPage">
        <ArrowLeft />
      </el-icon>
      <el-tag
        v-for="tag in paginatedTags"
        :key="tag"
        effect="light"
        @click="selectTag(tag as string)"
        :type="tag === activeTag ? 'success' : 'info'"
      >
        {{ tag }}
      </el-tag>
      <el-icon v-if="hasNextPage" class="arrow-icon" @click="nextPage">
        <ArrowRight />
      </el-icon>
    </div>
    <div class="search-bar">
      <el-input
        v-model="searchKeyword"
        placeholder="请输入关键字进行搜索"
        clearable
        class="search-input"
      ></el-input>
    </div>
    <div class="cards-grid" >
      <el-card v-for="(game, index) in paginatedGames" :key="index" class="game-card" @click="sendGameId(game.id)">
        <img :src="game.imageUrl" class="card-image" alt="game image" />
        <h3>{{ game.title }}</h3>
        <div class="card-tags">
          <el-tag size="small" v-for="tag in game.tags" :key="tag">{{ tag }}</el-tag>
        </div>
        <div class="card-time">{{ game.time }}</div>
      </el-card>


    </div>
    <div class="pagination-buttons">
      <el-button @click="prevGamePage" :disabled="nowpage === 1">上一页</el-button>
      <span>{{ nowpage }} / {{ Math.ceil(filteredGames.length / gamesPage) }}</span>
      <el-button @click="nextGamePage" :disabled="!hasNextGamePage">下一页</el-button>
    </div>
    
  </div>
</template>
<script setup lang="ts">
import { ref, computed, onMounted, watch } from "vue";
import { ArrowLeft, ArrowRight } from "@element-plus/icons-vue";
import ParticleBackground from '@/components/ParticleBackground.vue';
import router from "@/router";
import axios from "axios";  
import { useRoute } from "vue-router";
import Cookies from 'js-cookie'
const route = useRoute();
const handleAuth = () => {
  if (isLoggedIn.value) {
    logout();
  } else {
    router.push({ name: "LoginRegister" });
  }
};


const username = ref('');
const isLoggedIn = ref(false);

const checkLoginStatus = () => {
  const user = Cookies.get('user');
  isLoggedIn.value = !!user;
  if (user) {
    const userInfo = JSON.parse(user);
    username.value = userInfo.username;
  }
};

let gamesPage = 10; 
let nowpage = ref(1);
let paginatedGames = computed(() => {
  let start = (nowpage.value - 1) * gamesPage;
  let end = start + gamesPage;
  return filteredGames.value.slice(start, end);
});
let hasNextGamePage = computed(() => nowpage.value * gamesPage < filteredGames.value.length);
let nextGamePage = () => {
  if (hasNextGamePage.value) nowpage.value++;
};
let prevGamePage = () => {
  if (nowpage.value > 1) nowpage.value--;
};
const games = ref<any[]>([]);
const fetchGames = async () => {
  try {
    const response = await axios.get('http://localhost:8080/game/all');  
    games.value = response.data;  
  } catch (error) {
    console.error("获取游戏数据失败:", error);
  }
};
const searchKeyword = ref("");
const activeTag = ref("推荐");
const allTags = computed(() => {
  const tags = new Set();
  games.value.forEach((game) => game.tags.forEach((tag: unknown) => tags.add(tag)));
  return Array.from(tags);
});
const tagsPerPage = 8;
const currentPage = ref(1);
const paginatedTags = computed(() => {
  const start = (currentPage.value - 1) * tagsPerPage;
  const end = start + tagsPerPage;
  return allTags.value.slice(start, end);
});
const hasNextPage = computed(() => currentPage.value * tagsPerPage < allTags.value.length);
const nextPage = () => {
  if (hasNextPage.value) currentPage.value++;
};
const prevPage = () => {
  if (currentPage.value > 1) currentPage.value--;
};
const filteredGames = computed(() => {
  let filtered = games.value;
  if (activeTag.value === "推荐") {
    return [...games.value].sort((a, b) => a.heart - b.heart).slice(0,9);
  }
  if (activeTag.value !== "全部") {
    nowpage.value = 1; 
    filtered = filtered.filter((game) => game.tags.includes(activeTag.value));
  }
  if (searchKeyword.value) {
    nowpage.value = 1;
    filtered = filtered.filter(
      (game) =>
        game.title.includes(searchKeyword.value) ||
        game.tags.some((tag: string | string[]) => tag.includes(searchKeyword.value))
    );
  }
  return filtered;
});
const selectTag = (tag: string) => {
  activeTag.value = tag;
  nowpage.value = 1;  
};

  const sendGameId = async (gameId: number) => {
    try {
      let res = await axios.get(`http://localhost:8080/game/selectbyid?id=${gameId}`);
      const gameData = res.data;
      router.push({
        name: 'Test3',
        query: { gameData: JSON.stringify(gameData) }
      });
      
    } catch (error) {
      console.error("请求失败", error);
    }
  }   ;


  const logout = () => {
  Cookies.remove('user');
  isLoggedIn.value = false;
  router.push({ name: "LoginRegister" });
};
onMounted(() => {
    // const user = Cookies.get('user');
    // if (user) {
    //   const userInfo = JSON.parse(user);
    //   username.value = userInfo.username;
    // } else {
    //   console.log('没有找到用户信息');
    // }
    checkLoginStatus();
    fetchGames();
  });
watch(searchKeyword, () => {
  nowpage.value = 1; 
});
</script>

<style scoped>
.pagination-buttons {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
}

.pagination-buttons el-button {
  margin: 0 10px;
}

.pagination-buttons span {
  color: #fff;
  font-size: 1rem;
}

.app-container {
min-height: 100vh;
background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 100%); /* 透明黑色背景 */
color: #fff;
position: relative;
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

.category-bar {
  max-width: 1200px;
  margin: 1rem auto;
  display: flex;
  gap: 0.5rem;
  align-items: center;
  overflow-x: auto;
  padding: 0.5rem;
}

.active-tag {
  background-color: #ffd04b !important;
  color: #000 !important;
}

:deep(.el-tag) {
  background-color: #2c2c2c;
  border: none;
  color: #fff;
  cursor: pointer;
}

.cards-grid {
  max-width: 1200px;
  margin: 2rem auto;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(500px, 1fr));
  gap: 2rem;
  padding: 0 1rem;
}

.game-card {
  background-color: #1c1c1c;
  border: none;
  transition: transform 0.2s;
}

.game-card:hover {
  transform: translateY(-5px);
}

:deep(.el-card__body) {
  padding: 0;
}

.card-image {
  width: 100%;
  height: 160px;
  object-fit: cover;
}

h3 {
  margin: 1rem;
  font-size: 1rem;
  color: #fff;
}

.card-tags {
  margin: 1rem;
  display: flex;
  gap: 0.5rem;
  flex-wrap: wrap;
}

.card-time {
  margin: 1rem;
  color: #666;
  font-size: 0.875rem;
}

.search-bar {
  max-width: 1200px;
  margin: 1rem auto;
}

.search-input {
  width: 100%;
}

.ranking {
  flex: 1;
  padding: 20px;
  background: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  height: 400px;
  overflow-y: auto;
}

.ranking-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 10px;
  text-align: center;
}

.ranking ol {
  padding: 0;
  margin: 0;
  list-style: none;
}

.ranking-item {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  border-bottom: 1px solid #eee;
  cursor: pointer;
}

.ranking-item:last-child {
  border-bottom: none;
}

.heat {
  color: #ff5722;
  font-weight: bold;
}
</style>
