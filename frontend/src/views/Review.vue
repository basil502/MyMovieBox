<template>
  <Header />

  <!-- Banner -->
  <div class="banner">
    <h1>Movies</h1>
    <h2 class="banner-subtitle">Your Reviews & Ratings</h2>
    <p>📽️ Your watched list—stories told and memories made.</p>
  </div>

  <!-- Movie Grid -->
  <div class="list">
    <div class="movie-card" v-for="movie in toWatchList" :key="movie.id">
      <p>👤 {{ movie.user.name }}</p>
      <h3>{{ movie.title }}</h3>
      <p>🎭 {{ movie.genre }}</p>
      <p>🌐 {{ movie.language }}</p>
      <p>📅 {{ movie.releaseYear }}</p>
      <p style="color: black">📝 Review:</p>
      <p>{{ movie.description }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import Header from "../components/Header.vue";
import axios from "axios";

const toWatchList = ref([]);

onMounted(async () => {
  try {
    const { data } = await axios.get(`http://localhost:8086/getMoviesReview`);
    toWatchList.value = data;
  } catch {
    alert("Could not load movie list.");
  }
});
</script>

<style scoped>
.page {
  min-height: 100vh;
  padding: 2rem;
  background: linear-gradient(to right, #2b1055, #7597de);
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
  color: #ffffff;
}

.banner {
  background: rgba(0, 0, 0, 0.4);
  color: #ffffff;
  text-align: center;
  padding: 2rem;
  border-radius: 1rem;
  margin-bottom: 2rem;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.4);
  margin-top: 4rem;
  width: 1100px;
}

.banner h1 {
  font-size: 2.5rem;
  margin-bottom: 0.3rem;
  color: #ffd369;
}

.banner-subtitle {
  font-size: 1.3rem;
  margin-bottom: 0.5rem;
  color: #eee;
}

.banner p {
  font-size: 1.1rem;
  opacity: 0.9;
  color: #dddddd;
}

.list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 1.5rem;
}

.movie-card {
  background: rgba(255, 255, 255, 0.07);
  border: 1px solid rgba(255, 255, 255, 0.15);
  padding: 1.2rem;
  border-radius: 16px;
  box-shadow: 0 6px 14px rgba(0, 0, 0, 0.4);
  transition: transform 0.2s ease;
  display: flex;
  flex-direction: column;
  text-align: justify;
}

.movie-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 10px 24px rgba(0, 0, 0, 0.5);
}

.movie-card h3 {
  font-size: 1.4rem;
  margin-bottom: 0.6rem;
  color: #ffd369;
  margin-top: 0rem;
}

.movie-card p {
  font-size: 1rem;
  margin: 0.3rem 0;
  line-height: 1.4;
  color: #f0f0f0;
}

.movie-card p.reviewed-by {
  font-weight: bold;
  color: #98f5e1;
  margin-bottom: 0.6rem;
}

.movie-card p.label {
  color: #ffcccb;
  font-weight: 600;
  margin-top: 0.6rem;
  margin-bottom: 0.2rem;
}

.status {
  color: #48ff00;
}

</style>
