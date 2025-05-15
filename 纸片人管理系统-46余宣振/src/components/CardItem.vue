<template>
    <div class="card-item">
      <img :src="image" alt="卡片图片" class="card-image" />
      <div class="card-content">
        <h3>{{ title }}</h3>
        <p>{{ description }}</p>
        <div class="card-category">
          <span v-for="(category, index) in categories" :key="index">
            分类: {{ category }}
          </span>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    props: {
      image: String,
      title: String,
      description: String,
      categories: Array,
    },
  };
  </script>
  
  <style scoped>
  .card-item {
    border: 1px solid #ddd;
    border-radius: 8px;
    padding: 16px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }
  .card-image {
    width: 100%;
    height: auto;
    border-radius: 8px;
  }
  .card-content {
    margin-top: 8px;
  }
  .card-category span {
    margin-right: 8px;
    font-size: 0.9em;
    color: #007bff;
  }
  </style>
  