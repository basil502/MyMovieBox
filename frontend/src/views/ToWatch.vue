<script setup>
import { ref, onMounted } from "vue";
import Header from "../components/Header.vue";
import axios from "axios";
import { useForm, useField } from "vee-validate";
import * as yup from "yup";

const toWatchList = ref([]);
const selectedMovie = ref(null);
const newStatus = ref("");
const showModal = ref(false);

const schema = yup.object({
  completedNote: yup.string().when("status", {
    is: (val) => val === "Completed",
    then: yup
      .string()
      .required("Review is required when marking Completed")
      .min(10, "Please write at least 10 characters")
      .max(500, "Max 500 characters"),
    otherwise: yup.string().notRequired(),
  }),
});

const { handleSubmit, resetForm } = useForm({
  validationSchema: schema,
  initialValues: { completedNote: "" },
});
const { value: completedNote, errorMessage: completedNoteError } = useField(
  "completedNote"
);

const fetchMovies = async () => {
  const userId = localStorage.getItem("userId");
  try {
    const { data } = await axios.get(
      `http://localhost:8086/getMoviesByStatus?userId=${userId}`
    );
    toWatchList.value = data;
  } catch {
    alert("Could not load movie list.");
  }
};

onMounted(fetchMovies);

const openEditModal = async (id) => {
  const { data } = await axios.get(`http://localhost:8086/getMovie/${id}`);
  selectedMovie.value = data;
  newStatus.value = data.status;
  resetForm();
  showModal.value = true;
};

const updateStatus = async () => {
  if (newStatus.value === "Completed" && completedNote.value.trim().length < 10) {
    return alert("Please write at least 10 characters for your review.");
  }

  await axios.post(
    `http://localhost:8086/updateMovie/${selectedMovie.value.id}`,
    {
      status: newStatus.value,
      description: completedNote.value,
    },
    { headers: { "Content-Type": "application/json" } }
  );

  showModal.value = false;
  await fetchMovies();
};

const MovieDelete = async (id) => {
  const confirmDelete = confirm("Are you sure you want to delete this movie?");
  if (!confirmDelete) return;
  try {
    await axios.delete(`http://localhost:8086/deleteMovie/${id}`);
    toWatchList.value = toWatchList.value.filter((movie) => movie.id !== id);
  } catch (error) {
    alert("Failed to delete the movie." + error);
  }
};
</script>
<template>
  <Header />

  <div class="banner">
    <h1>📺 To Watch List</h1>
    <p>Movies you plan to watch soon</p>
  </div>

  <div class="list">
    <div
      class="movie-card"
      v-for="(movie, idx) in toWatchList"
      :key="idx"
      @click="openEditModal(movie.id)"
      style="cursor: pointer"
    >
      <button type="button" class="button" @click.stop="MovieDelete(movie.id)">🗑️</button>
      <h3>{{ movie.title }}</h3>
      <p>🎭 {{ movie.genre }}</p>
      <p>🌐 {{ movie.language }}</p>
      <p :style="{ color: movie.status === 'In Progress' ? 'red' : 'orange' }">
        {{ movie.status }}
      </p>
      <p>📅 {{ movie.releaseYear }}</p>
    </div>
  </div>

  <Teleport to="body">
    <div v-if="showModal" class="modal-backdrop" @click.self="showModal = false">
      <div class="modal-card">
        <h2>Edit Movie Status</h2>
        <h3>{{ selectedMovie.title }}</h3>
        <p>🎭 {{ selectedMovie.genre }}</p>
        <p>🌐 {{ selectedMovie.language }}</p>
        <p>📅 {{ selectedMovie.releaseYear }}</p>

        <select v-model="newStatus">
          <option disabled value="">Select Status</option>
          <option>To Watch</option>
          <option>In Progress</option>
          <option>Completed</option>
        </select>

        <!-- show textbox + VeeValidate error only when Completed -->
        <div v-if="newStatus === 'Completed'">
          <input
            type="text"
            v-model="completedNote"
            :placeholder="completedNoteError || 'Write your review...'"
            :class="{ 'input-error': completedNoteError }"
          />
          <p v-if="completedNoteError" class="error-text">
            {{ completedNoteError }}
          </p>
        </div>

        <div style="margin-top: 1rem">
          <button @click="updateStatus">✅ Update</button>
          <button @click="showModal = false" style="margin-left: 1rem">❌ Cancel</button>
        </div>
      </div>
    </div>
  </Teleport>
</template>

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
  margin-bottom: 0.5rem;
}

.banner p {
  font-size: 1.1rem;
  opacity: 0.9;
}

.list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 1.5rem;
}

.movie-card {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.15);
  padding: 1.2rem;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.4);
  transition: transform 0.2s ease;
}

.movie-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.5);
}

.movie-card h3 {
  font-size: 1.3rem;
  margin-bottom: 0.5rem;
  color: #ffd369;
  margin-top: -1rem;
}

.movie-card p {
  font-size: 1rem;
  margin: 0.3rem 0;
  color: #eeeeee;
}

/* Modal styles */
.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(15, 15, 30, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-card {
  background-color: #2b1055;
  padding: 1.5rem;
  border-radius: 12px;
  width: 300px;
  color: white;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.5);
  text-align: center;
}
.modal-card h3 {
  font-size: 1.3rem;
  margin-bottom: 0.5rem;
  color: #ffd369;
}
.modal-card select {
  width: 100%;
  padding: 0.5rem;
  margin-top: 1rem;
  font-size: 1rem;
  border-radius: 8px;
}

.modal-card button {
  padding: 0.5rem 1rem;
  background-color: #ffd369;
  color: #2b1055;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: bold;
}

.modal-card button:hover {
  background-color: #ffbe33;
}
/* Style for the review textbox in the modal */
.modal-card input[type="text"] {
  width: 100%;
  padding: 0.5rem 0.75rem;
  margin-top: 1rem;
  font-size: 1rem;
  border: 1px solid rgba(255, 255, 255, 0.4);
  border-radius: 8px;
  background-color: white;
  color: #000000;
  outline: none;
  box-sizing: border-box;
  transition: border-color 0.2s ease, box-shadow 0.2s ease;
}

.modal-card input[type="text"]::placeholder {
  color: rgba(0, 0, 0, 0.6);
}

.modal-card input[type="text"]:focus {
  border-color: #ffd369;
  box-shadow: 0 0 8px rgba(255, 211, 105, 0.5);
}
.input-error {
  border: 1px solid red;
  color: red;
}
.button {
  margin-left: 11rem;
  background: none;
  outline: none;
  margin-top: -18px;
  border: none;
}
</style>
