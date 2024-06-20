<script setup>
import { ref } from "vue";
import { participants } from "@/stores/mapStore";

defineProps({
    msg: String,
});

const count = ref(0);

onMounted(() => {
    // fetch("https://org.maziarz.org/api/participants")
    //     .then((response) => response.json())
    //     .then((data) => {
    //         console.log(data);
    //     });
});
</script>

<template>
    <div class="card">
        <button type="button" @click="count++">count is {{ count }}</button>
    </div>
</template>

<style scoped>
.read-the-docs {
    color: #888;
}
</style>
