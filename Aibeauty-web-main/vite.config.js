// vite.config.js
import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';

export default defineConfig({
  plugins: [vue()],
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // 后端服务地址
        changeOrigin: true, // 推荐设置为true，以便代理可以正常工作
        rewrite: (path) => path.replace(/^\/api/, '') // 移除请求路径中的/api前缀
      },
    }
  },
});
