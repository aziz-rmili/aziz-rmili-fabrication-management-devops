# ================================
# ÉTAPE 1 : BUILD
# On utilise Node.js pour compiler Angular
# ================================
FROM node:20-alpine AS build

WORKDIR /app

# Copier package.json pour installer les dépendances
COPY package*.json .
RUN npm install

# Copier tout le code source Angular
COPY . .

# Compiler Angular en mode production
RUN npm run build

# ================================
# ÉTAPE 2 : RUN
# Nginx sert les fichiers statiques compilés
# ================================
FROM nginx:alpine

# Supprimer la config Nginx par défaut
RUN rm -rf /usr/share/nginx/html/*

# Copier les fichiers Angular compilés vers Nginx
COPY --from=build /app/dist/frontend/browser /usr/share/nginx/html

# Copier notre config Nginx personnalisée
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Exposer le port 80
EXPOSE 80

# Nginx démarre automatiquement
CMD ["nginx", "-g", "daemon off;"]
