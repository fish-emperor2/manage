<template>
    <canvas ref="canvas" class="particle-canvas"></canvas>
  </template>
  
  <script setup>
  import { ref, onMounted, onUnmounted } from 'vue';
  
  const canvas = ref(null);
  let ctx = null;
  let animationFrameId = null;
  let particles = [];
  
  class Particle {
    constructor(x, y) {
      this.x = x;
      this.y = y;
      this.size = Math.random() * 2 + 1;
      this.speedX = Math.random() * 1 - 0.5;
      this.speedY = Math.random() * 1 - 0.5;
      this.opacity = Math.random() * 0.5 + 0.2;
    }
  
    update() {
      this.x += this.speedX;
      this.y += this.speedY;
  
      if (this.x < 0 || this.x > window.innerWidth) this.speedX *= -1;
      if (this.y < 0 || this.y > window.innerHeight) this.speedY *= -1;
    }
  
    draw() {
      ctx.beginPath();
      ctx.arc(this.x, this.y, this.size, 0, Math.PI * 2);
      ctx.fillStyle = `rgba(255, 255, 255, ${this.opacity})`;
      ctx.fill();
    }
  }
  
  const initParticles = () => {
    particles = [];
    const numberOfParticles = Math.floor((window.innerWidth * window.innerHeight) / 10000);
    
    for (let i = 0; i < numberOfParticles; i++) {
      const x = Math.random() * window.innerWidth;
      const y = Math.random() * window.innerHeight;
      particles.push(new Particle(x, y));
    }
  };
  
  const animate = () => {
    ctx.clearRect(0, 0, window.innerWidth, window.innerHeight);
    
    particles.forEach(particle => {
      particle.update();
      particle.draw();
    });
    
    animationFrameId = requestAnimationFrame(animate);
  };
  
  const handleResize = () => {
    if (canvas.value) {
      canvas.value.width = window.innerWidth;
      canvas.value.height = window.innerHeight;
      initParticles();
    }
  };
  
  onMounted(() => {
    if (canvas.value) {
      ctx = canvas.value.getContext('2d');
      canvas.value.width = window.innerWidth;
      canvas.value.height = window.innerHeight;
      
      initParticles();
      animate();
      
      window.addEventListener('resize', handleResize);
    }
  });
  
  onUnmounted(() => {
    if (animationFrameId) {
      cancelAnimationFrame(animationFrameId);
    }
    window.removeEventListener('resize', handleResize);
  });
  </script>
  
  <style scoped>
  .particle-canvas {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    pointer-events: none;
    z-index: 1;
  }
  </style>
  