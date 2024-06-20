<script setup>
import { ref } from "vue";
import { participants, chosenParticipant } from "@/stores/mapStore";
import { onMounted, watch } from "vue";

defineProps({
    msg: String,
});

const count = ref(0);
const options = ref([]);

onMounted(() => {
    fetch("https://org.maziarz.org/api/participants?top=20")
        .then((response) => response.json())
        .then((data) => {
            console.log(data);
            options.value = data;
        });
});

watch(chosenParticipant, (newValue) => {
    console.log("Selected option changed to:", newValue);
});
</script>

<template>
    <div class="card">
        <div>
            <label for="selectControl">Wiślak:</label>
            <select id="selectControl" v-model="chosenParticipant">
                <option
                    v-for="option in options"
                    :key="option.id"
                    :value="option.id"
                >
                    {{ option.name }} ({{ option.hours }}h)
                </option>
            </select>
            Id: {{ chosenParticipant }}
        </div>
    </div>
</template>

<style scoped>
.read-the-docs {
    color: #888;
}
.card {
    padding: 2px;
}
</style>
