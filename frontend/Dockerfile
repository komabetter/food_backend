# Use Node.js official image
FROM node:20-alpine

# Set the working directory
WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm ci

# Copy the rest of the application code
COPY . .

# Expose port 4200
EXPOSE 4200

# Start the Angular development server
CMD ["npm", "start"]