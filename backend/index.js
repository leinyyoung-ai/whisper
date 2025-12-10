require('dotenv').config();
const express = require('express');
const crypto = require('crypto');
const cors = require('cors');

const app = express();
const port = process.env.PORT || 3001; // Use port from .env or default to 3001

app.use(cors()); // Enable CORS for all routes

// Function to generate the authenticated URL for iFLYTEK's WebSocket API
function getXunfeiAuthUrl(host, path, apiKey, apiSecret) {
  const date = new Date().toUTCString();
  const signatureOrigin = `host: ${host}\ndate: ${date}\nGET ${path} HTTP/1.1`;

  const signatureSha = crypto
    .createHmac('sha256', apiSecret)
    .update(signatureOrigin)
    .digest('base64');

  const authorizationOrigin = `api_key="${apiKey}", algorithm="hmac-sha256", headers="host date request-line", signature="${signatureSha}"`;
  const authorization = Buffer.from(authorizationOrigin).toString('base64');

  const url = `wss://${host}${path}?authorization=${authorization}&date=${encodeURI(date)}&host=${host}`;
  return url;
}

app.get('/api/get-asr-url', (req, res) => {
  // iFLYTEK's IAT (speech-to-text) API endpoint details
  const host = 'iat-api.xfyun.cn';
  const path = '/v2/iat';
  const apiKey = process.env.XF_API_KEY;
  const apiSecret = process.env.XF_API_SECRET;

  if (!apiKey || !apiSecret) {
    return res.status(500).json({ error: 'API key and secret are not configured.' });
  }

  const url = getXunfeiAuthUrl(host, path, apiKey, apiSecret);
  res.json({ url });
});

app.listen(port, () => {
  console.log(`Server listening at http://localhost:${port}`);
});
