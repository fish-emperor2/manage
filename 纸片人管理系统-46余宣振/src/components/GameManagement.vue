<template>
    <div class="game-management">
      <h2 class="page-title">游戏管理</h2>
      <div class="controls">
        <el-input
          v-model="searchQuery"
          placeholder="搜索游戏名称或标签"
          class="search-input"
          @input="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-button type="primary" @click="showCreateGameDialog">新增游戏</el-button>
      </div>
      <el-table :data="filteredGames" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="标题" width="200" />
        <el-table-column label="标签" width="200">
          <template #default="{ row }">
            <el-tag v-for="tag in row.tags" :key="tag" class="mx-1">
              {{ tag }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="heart" label="热度" width="100" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button type="primary" @click="showDetails(row)">详情</el-button>
            <el-button type="danger" @click="deleteGame(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
  
      <el-dialog v-model="dialogVisible" :title="dialogTitle" width="70%">
        <el-form :model="currentGame" label-width="100px">
          <el-form-item label="ID">
            <el-input v-model="currentGame.id" disabled />
          </el-form-item>
          <el-form-item label="标题">
            <el-input v-model="currentGame.title" />
          </el-form-item>
          <el-form-item label="副标题">
            <el-input v-model="currentGame.subtitle" />
          </el-form-item>
          <el-form-item label="品牌">
            <el-input v-model="currentGame.brand" />
          </el-form-item>
          <el-form-item label="发布日期">
            <el-date-picker v-model="currentGame.releaseDate" type="date" placeholder="选择日期" />
          </el-form-item>
          <el-form-item label="游戏类型">
            <el-input v-model="currentGame.gameType" />
          </el-form-item>
          <el-form-item label="主视图">
          <el-image
            :src="currentGame.imageUrl"
            :preview-src-list="[currentGame.imageUrl]"
            fit="cover"
            style="width: 200px; height: 200px;"
          >
            <template #error>
              <div class="image-slot">
                <el-icon><icon-picture /></el-icon>
              </div>
            </template>
          </el-image>
          <el-upload
            class="upload-demo"
           
            :before-upload="beforeUploadMainImage"
            :show-file-list="false"
          >
            <el-button type="primary">更换主视图</el-button>
          </el-upload>
        </el-form-item>


          <el-form-item label="预览图">
            <div class="preview-images">
              <div v-for="(image, index) in currentGame.gameImage" :key="index" class="preview-image-item">
                <el-image 
                  :src="image.imageUrl" 
                  :preview-src-list="currentGame.gameImage.map(image => image.imageUrl)" 
                  fit="cover" 
                  style="width: 100px; height: 100px;">
                  <template #error>
                    <div class="image-slot">
                      <el-icon><icon-picture /></el-icon>
                    </div>
                  </template>
                </el-image>
                <el-button type="danger" size="small" @click="deletePreviewImage(index,image.imageUrl,currentGame.id)">删除</el-button>
              </div>
            </div>
            <el-upload
              class="upload-demo"
              :action="uploadAction2"
              :before-upload="beforeUploadPreviewImage"
              :show-file-list="false"
            >
              <el-button type="primary">添加预览图</el-button>
            </el-upload>
          </el-form-item>

          <el-form-item label="创建时间">
            <el-input v-model="currentGame.createdAt" disabled />
          </el-form-item>
          <el-form-item label="更新时间">
            <el-input v-model="currentGame.updatedAt" disabled />
          </el-form-item>
          <el-form-item label="热度">
            <el-input-number v-model="currentGame.heart" :min="0" />
          </el-form-item>
          <el-form-item label="故事简介">
            <el-input v-model="currentGame.storyIntro" type="textarea" :rows="4" />
          </el-form-item>
          <el-form-item label="标签">
            <el-select
              v-model="currentGame.tags"
              multiple
              filterable
              allow-create
              default-first-option
              placeholder="请选择或输入标签"
            >
              <el-option
                v-for="tag in allTags"
                :key="tag"
                :label="tag"
                :value="tag"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="评论">
            <el-table :data="currentGame.comments" style="width: 100%">
              <el-table-column prop="username" label="用户" width="120" />
              <el-table-column prop="content" label="内容" />

              <el-table-column prop="timestamp" :formatter="(row) => formatTimestamp(row.timestamp)" label="时间" width="180" />
                <el-table-column label="操作" width="120">
                <template #default="{ row, $index }">
                  <el-button type="danger" size="small" @click="deleteComment(row.id)">删除</el-button>
                </template>

              </el-table-column>
            </el-table>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="saveGame">保存</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </template>                                                                                           <script setup lang="ts">
  import { ref, reactive, onMounted, computed } from 'vue'
  import { ElMessage, ElMessageBox } from 'element-plus'
  import { Picture as IconPicture, Search } from '@element-plus/icons-vue'
  import axios from 'axios'
  
  interface Comment {
    username: string
    content: string
    timestamp: string
    id:string
  }
  
  interface GameData {
    id: number
    title: string
    subtitle: string
    brand: string
    releaseDate: string
    gameType: string
    imageUrl: string
    gameImage: { imageUrl: string }[]
    createdAt: string
    updatedAt: string
    heart: number
    storyIntro: string
    tags: string[]
    comments: Comment[]
  }
  
  const games = ref<GameData[]>([])
  const searchQuery = ref('')
  const dialogVisible = ref(false)
  const dialogTitle = ref('')
  const currentGame = reactive<GameData>({
    id: 0,
    title: "",
    subtitle: "",
    brand: "",
    releaseDate: "",
    gameType: "",
    imageUrl: "",
    gameImage: [],
    createdAt: "",
    updatedAt: "",
    heart: 0,
    storyIntro: "",
    tags: [],
    comments: []
  })
  
  onMounted(() => {
    fetchGames()
  })
  
  const fetchGames = async () => {
    try {
      const response = await axios.get('http://localhost:8080/game/all')
      games.value = response.data
    } catch (error) {
      console.error("获取游戏数据失败:", error)
      ElMessage.error('获取游戏数据失败')
    }
  }
  
  const allTags = computed(() => {
    const tags = new Set()
    games.value.forEach((game) => game.tags.forEach((tag: string) => tags.add(tag)))
    return Array.from(tags) as string[]
  })
  
  const filteredGames = computed(() => {
    if (!searchQuery.value) return games.value
    return games.value.filter(game => 
      game.title.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      game.tags.some(tag => tag.toLowerCase().includes(searchQuery.value.toLowerCase()))
    )
  })
  
  const handleSearch = () => {
  }
  
  const showCreateGameDialog = () => {
    Object.assign(currentGame, {
      id: 0,
      title: "",
      subtitle: "",
      brand: "",
      releaseDate: "",
      gameType: "",
      imageUrl: "",
      gameImage: [],
      createdAt: "",
      updatedAt: "",
      heart: 0,
      storyIntro: "",
      tags: [],
      comments: []
    })
    dialogTitle.value = '新增游戏'
    dialogVisible.value = true
  }
  
  const showDetails = async (game: GameData) => {
    try {
      const [gameData, gameImages, gameComments] = await Promise.all([
        axios.get(`http://localhost:8080/game/selectbyid?id=${game.id}`),
        axios.get(`http://localhost:8080/screenshot/selectByid?id=${game.id}`),
        axios.get(`http://localhost:8080/comment/selectByid?id=${game.id}`)
      ])
      Object.assign(currentGame, gameData.data)
      currentGame.gameImage = gameImages.data
      currentGame.comments = gameComments.data
      dialogTitle.value = '游戏详情'
      dialogVisible.value = true
    } catch (error) {
      console.error('获取游戏详情失败:', error)
      ElMessage.error('获取游戏详情失败，请稍后再试')
    }
  }
  
  const saveGame = async () => {
    try {
      const updatedGame = {
        id: currentGame.id,
        title: currentGame.title,
        subtitle: currentGame.subtitle,
        brand: currentGame.brand,
        releaseDate: currentGame.releaseDate ? new Date(currentGame.releaseDate).toISOString() : null,
        gameType: currentGame.gameType,
        imageUrl: currentGame.imageUrl,
        createdAt: currentGame.createdAt ? new Date(currentGame.createdAt).toISOString() : null,
        updatedAt: new Date().toISOString(),
        heart: currentGame.heart,
        storyIntro: currentGame.storyIntro,
        tags: currentGame.tags,
           }
      const updatedImage={
        gameId:currentGame.id,
        gameImage:currentGame.gameImage
      }
      let response
      if (currentGame.id === 0) {
        response = await axios.post('http://localhost:8080/game/insert', updatedGame)

      } else {
        response = await axios.post('http://localhost:8080/game/update', updatedGame)
      }
  
      if (response.data) {
        ElMessage.success(currentGame.id === 0 ? '游戏创建成功' : '游戏信息已更新')
        fetchGames()
      } else {
        ElMessage.error(currentGame.id === 0 ? '创建失败' : '更新失败')
      }
  
      dialogVisible.value = false
    } catch (error) {
      console.error('保存游戏信息失败:', error)
      ElMessage.error('保存游戏信息失败，请稍后再试')
    }
  }
  
  const deleteGame = (game: GameData) => {
    ElMessageBox.confirm(
      `确定要删除游戏 "${game.title}" 吗？`,
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    ).then(async () => {
      try {
        await axios.delete(`http://localhost:8080/game/delete?id=${game.id}`)
        ElMessage.success('游戏删除成功')
        fetchGames()
      } catch (error) {
        console.error('删除游戏失败:', error)
        ElMessage.error('删除游戏失败，请稍后再试')
      }
    }).catch(() => {
      ElMessage.info('已取消删除')
    })
  }
  function deleteComment(id: string) {
  axios.get(`http://localhost:8080/comment/delete?id=${id}`)
    .then(() => {
      ElMessage.success('评论删除成功')
      const index = currentGame.comments.findIndex(comment => comment.id === id)
      if (index !== -1) {
        currentGame.comments.splice(index, 1)
      }
    })
    .catch((error) => {
      console.error('删除评论失败:', error)
      ElMessage.error('删除评论失败，请稍后再试')
    })
}

  
  const uploadAction = 'http://localhost:8080/upload/image'
  const uploadAction2 = 'http://localhost:8080/upload/screenshot'  
  const handleMainImageSuccess = async (file: File) => {
  const formData = new FormData()
  formData.append('file', file)
  
  try {
    const response = await axios.post(uploadAction, formData)
    currentGame.imageUrl = response.data.url
    alert(currentGame.imageUrl)
    ElMessage.success('主视图上传成功')
  } catch (error) {
    console.error('主视图上传失败:', error)
    ElMessage.error('主视图上传失败')
  }
}

const handlePreviewImageSuccess = async (file: File) => {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('gameId', currentGame.id.toString())
  
  try {
    const response = await axios.post(uploadAction2, formData, {

    })
    currentGame.gameImage.push({ imageUrl: response.data.url })
    ElMessage.success('预览图上传成功')
  } catch (error) {
    console.error('预览图上传失败:', error)
    ElMessage.error('预览图上传失败')
  }
}

  
  const deletePreviewImage = (index: number ,url:string,id:number) => {
    currentGame.gameImage.splice(index, 1)
    axios.get(`http://localhost:8080/screenshot/delete?url=${url}&id=${id}`)
    ElMessage.success('预览图删除成功')
  }
  
  const formatTimestamp = (timestamp: string) => {
    const date = new Date(timestamp)
    return date.toLocaleString()
  }
  const beforeUploadMainImage = (file: File) => {
  handleMainImageSuccess(file)
  return false 
}

const beforeUploadPreviewImage = (file: File) => {
  handlePreviewImageSuccess(file)
  return false 
}

  </script>
  
<style scoped>
  .game-management {
    padding: 20px;
  }
  
  .page-title {
    font-size: 24px;
    margin-bottom: 20px;
  }
  
  .controls {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;
  }
  
  .search-input {
    width: 300px;
  }
  
  .el-tag {
    margin-right: 5px;
  }
  
  .preview-images {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    margin-bottom: 10px;
  }
  
  .preview-image-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 5px;
  }
  
  .image-slot {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    background: #f5f7fa;
    color: #909399;
    font-size: 30px;
  }
  
  .upload-demo {
    margin-top: 10px;
  }
</style>
  
  