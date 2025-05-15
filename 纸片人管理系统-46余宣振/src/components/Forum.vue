<template>
  <div class="forum">
    <h2>论坛</h2>
    <div class="new-post">
      <el-input v-model="newPost.title" placeholder="帖子标题" />
      <el-input
        type="textarea"
        v-model="newPost.content"
        placeholder="帖子内容"
        rows="4"
      />
      <el-button type="primary" @click="submitPost">发布</el-button>
    </div>
    <el-table :data="posts" style="width: 100%">
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="content" label="内容" />
      <el-table-column label="操作" width="120">
        <template #default="{ row }">
          <el-button type="danger" size="small" @click="deletePost(row.id)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';

interface Post {
  id: number;
  title: string;
  content: string;
}

const posts = ref<Post[]>([]);
const newPost = ref({ title: '', content: '' });

const fetchPosts = async () => {
  try {
    const response = await axios.get('http://localhost:8080/post/all');
    posts.value = response.data;
  } catch (error) {
    console.error('获取帖子失败:', error);
    ElMessage.error('获取帖子失败，请稍后再试');
  }
};

const submitPost = async () => {
  if (!newPost.value.title || !newPost.value.content) {
    ElMessage.warning('标题和内容不能为空');
    return;
  }
  try {
    const response = await axios.post('http://localhost:8080/post/add', newPost.value);
    if (response.data) {
      ElMessage.success('帖子发布成功');
      newPost.value = { title: '', content: '' };
      fetchPosts();
    } else {
      ElMessage.error('帖子发布失败');
    }
  } catch (error) {
    console.error('发布帖子失败:', error);
    ElMessage.error('发布帖子失败，请稍后再试');
  }
};

const deletePost = async (postId: number) => {
  try {
    const response = await axios.delete(`http://localhost:8080/post/delete?id=${postId}`);
    if (response.data) {
      ElMessage.success('帖子删除成功');
      fetchPosts();
    } else {
      ElMessage.error('帖子删除失败');
    }
  } catch (error) {
    console.error('删除帖子失败:', error);
    ElMessage.error('删除帖子失败，请稍后再试');
  }
};

onMounted(() => {
  fetchPosts();
});
</script>

<style scoped>
.forum {
  padding: 20px;
}

.new-post {
  margin-bottom: 20px;
}

.new-post el-input {
  margin-bottom: 10px;
}
</style> 