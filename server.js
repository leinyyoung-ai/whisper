// server.js (Node.js/Express 示例)
require('dotenv').config(); // 如果使用 .env 文件存储密钥
const express = require('express');
const crypto = require('crypto');
const path = require('path');
const app = express();
const port = 3001;

app.use(express.static(__dirname));

// 建议从环境变量中读取密钥
const XF_API_KEY = process.env.XF_API_KEY || 'a03d3f4db1990888913ac5bec3654ca7';
const XF_API_SECRET = process.env.XF_API_SECRET || 'NmE3Zjg4OWUwOGMwM2RjZGQ0NWNjYjZj';
// const XF_APP_ID = process.env.XF_APP_ID || '8cf6c216'; // APPID主要用于初始化请求体，URL鉴权用不到

/**
 * 讯飞 Web API鉴权签名生成函数
 * @param {string} host - API的主机名，例如 'iat-api.xfyun.cn'
 * @param {string} path - API的路径，例如 '/v2/iat'
 * @param {string} apiKey - 您的APIKey
 * @param {string} apiSecret - 您的APISecret
 * @returns {string} 完整的 WebSocket 鉴权 URL
 */
function getXunfeiAuthUrl(host, path, apiKey, apiSecret) {
    const date = new Date().toUTCString();

    // 1. 构造待签名字符串
    const signatureOrigin = `host: ${host}\ndate: ${date}\nGET ${path} HTTP/1.1`;

    // 2. 使用 HMAC-SHA256 签名
    const signatureSha = crypto
        .createHmac('sha256', apiSecret)
        .update(signatureOrigin)
        .digest('base64');

    // 3. 构造 authorization 原始字符串
    const authorizationOrigin = `api_key="${apiKey}", algorithm="hmac-sha256", headers="host date request-line", signature="${signatureSha}"`;

    // 4. Base64编码 authorization
    const authorization = Buffer.from(authorizationOrigin).toString('base64');

    // 5. 拼接最终的 WebSocket URL
    // 注意：所有参数都需要 URI 编码
    const url = `wss://${host}${path}?authorization=${authorization}&date=${encodeURI(date)}&host=${host}`;

    return url;
}

// -------------------------------------------------------------
// API 路由: 用于前端获取 ASR (语音听写) 服务的鉴权 URL
// -------------------------------------------------------------
app.get('/api/get-asr-url', (req, res) => {
    try {
        const host = 'iat-api.xfyun.cn'; // 语音听写服务的 Host
        const path = '/v2/iat';         // 语音听写服务的 Path

        const url = getXunfeiAuthUrl(host, path, XF_API_KEY, XF_API_SECRET);

        // 成功返回鉴权 URL
        res.json({ url });
    } catch (error) {
        console.error("鉴权 URL 生成失败:", error);
        res.status(500).json({ error: 'Failed to generate auth URL' });
    }
});

app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'index.html'));
});

app.listen(port, () => {
  console.log(`Node.js server listening at http://localhost:${port}`);
  console.log(`请确保前端访问此端口以获取鉴权 URL。`);
});
