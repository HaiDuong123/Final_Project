require("dotenv").config()

const express = require("express")
const mongoose = require("mongoose")
const bodyParser = require("body-parser")
const cors = require("cors")
const bcrypt = require("bcrypt")

const app = express()

app.use(bodyParser.json())
app.use(cors())

// ================= MongoDB =================
mongoose.connect(process.env.MONGO_URI)
.then(() => console.log("MongoDB Connected"))
.catch(err => console.log("Mongo Error:", err))

// ================= Schema =================
const AccountSchema = new mongoose.Schema({
    username: { type: String, required: true, unique: true },
    password: { type: String, required: true },
    email: String
})

const Account = mongoose.model("accounts", AccountSchema)


// ================= REGISTER =================
app.post("/register", async (req, res) => {
    try {
        const { username, password, email } = req.body

        if (!username || !password) {
            return res.json({
                ok: false,
                message: "Thiếu username hoặc password"
            })
        }

        const exist = await Account.findOne({ username })

        if (exist) {
            return res.json({
                ok: false,
                message: "Username đã tồn tại"
            })
        }

        // 🔐 hash password
        const hashedPassword = await bcrypt.hash(password, 10)

        const account = new Account({
            username,
            password: hashedPassword,
            email
        })

        await account.save()

        res.json({
            ok: true,
            message: "Register success",
            data: account
        })

    } catch (err) {
        res.json({
            ok: false,
            message: err.message
        })
    }
})


// ================= LOGIN =================
app.post("/login", async (req, res) => {
    try {
        const { username, password } = req.body

        const user = await Account.findOne({ username })

        if (!user) {
            return res.json({
                ok: false,
                message: "Sai tài khoản hoặc mật khẩu"
            })
        }

        // 🔐 so sánh password
        const isMatch = await bcrypt.compare(password, user.password)

        if (!isMatch) {
            return res.json({
                ok: false,
                message: "Sai tài khoản hoặc mật khẩu"
            })
        }

        res.json({
            ok: true,
            message: "Login success",
            data: user
        })

    } catch (err) {
        res.json({
            ok: false,
            message: err.message
        })
    }
})


// ================= CHANGE PASSWORD =================
app.post("/change-password", async (req, res) => {
    try {
        const { username, newPassword } = req.body

        if (!username || !newPassword) {
            return res.json({
                ok: false,
                message: "Thiếu dữ liệu"
            })
        }

        const user = await Account.findOne({ username })

        if (!user) {
            return res.json({
                ok: false,
                message: "User không tồn tại"
            })
        }

        // 🔐 hash password mới
        const hashedPassword = await bcrypt.hash(newPassword, 10)

        user.password = hashedPassword
        await user.save()

        return res.json({
            ok: true,
            message: "Đổi mật khẩu thành công"
        })

    } catch (err) {
        return res.json({
            ok: false,
            message: err.message
        })
    }
})


// ================= TEST API =================
app.get("/", (req, res) => {
    res.send("API is running 🚀")
})


// ================= START SERVER =================
const PORT = process.env.PORT || 3000

app.listen(PORT, () => {
    console.log("Server running on port " + PORT)
})