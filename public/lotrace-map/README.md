# Vue 3 + Vite

This template should help get you started developing with Vue 3 in Vite. The template uses Vue 3 `<script setup>` SFCs, check out the [script setup docs](https://v3.vuejs.org/api/sfc-script-setup.html#sfc-script-setup) to learn more.

## Recommended IDE Setup

- [VS Code](https://code.visualstudio.com/) + [Vue - Official](https://marketplace.visualstudio.com/items?itemName=Vue.volar) (previously Volar) and disable Vetur



SELECT latitude, longitude, elevation FROM `w23_elevations` group by latitude, longitude, elevation



LEAST(ST_LineLocatePoint((SELECT route FROM routes WHERE id = 1), ST_GeomFromText('POINT(18.99669560046338	49.627864784785686)')), ST_LineLocatePoint((SELECT route FROM routes WHERE id = 1), ST_GeomFromText('POINT(49.86944992783505	49.59714460399759)')))

GREATEST(ST_LineLocatePoint((SELECT route FROM routes WHERE id = 1), ST_GeomFromText('POINT(18.99669560046338	49.627864784785686)')), ST_LineLocatePoint((SELECT route FROM routes WHERE id = 1), ST_GeomFromText('POINT(49.86944992783505	49.59714460399759)')))


-- Compute the distance along the LineString
SELECT ST_Length(ST_LineSubstring( (SELECT route FROM routes WHERE id = 1), LEAST(ST_LineLocatePoint((SELECT route FROM routes WHERE id = 1), ST_GeomFromText('POINT(18.99669560046338	49.627864784785686)')), ST_LineLocatePoint((SELECT route FROM routes WHERE id = 1), ST_GeomFromText('POINT(49.86944992783505	49.59714460399759)'))), GREATEST(ST_LineLocatePoint((SELECT route FROM routes WHERE id = 1), ST_GeomFromText('POINT(18.99669560046338	49.627864784785686)')), ST_LineLocatePoint((SELECT route FROM routes WHERE id = 1), ST_GeomFromText('POINT(49.86944992783505	49.59714460399759)'))))) AS distance;
