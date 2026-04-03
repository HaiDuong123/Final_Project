const express = require("express")
const mongoose = require("mongoose")
const bodyParser = require("body-parser")

const app = express()

app.use(bodyParser.json())

// ================= MongoDB =================
mongoose.connect(
"mongodb+srv://Buinhathuy12345DB:Lienquanmoba123@cluster0.f6hfvik.mongodb.net/account"
)
.then(() => console.log("MongoDB Connected"))
.catch(err => console.log(err))


// ================= Schema =================
const AccountSchema = new mongoose.Schema({
    username: String,
    password: String,
    email: String
})

const Account = mongoose.model("accounts", AccountSchema)


// ================= REGISTER =================
app.post("/register", async (req, res) => {

    try {

        const { username, password, email } = req.body

        const exist = await Account.findOne({ username })

        if (exist) {
            return res.json({
                ok:false,
                message:"Username đã tồn tại"
            })
        }

        const account = new Account({
            username,
            password,
            email
        })

        await account.save()

        res.json({
            ok:true,
            message:"Register success",
            data: account
        })

    } catch(err){
        res.json({
            ok:false,
            message:err.message
        })
    }

})


// ================= LOGIN =================
app.post("/login", async (req, res) => {

    const { username, password } = req.body

    const user = await Account.findOne({
        username,
        password
    })

    if(!user){
        return res.json({
            ok:false,
            message:"Sai tài khoản hoặc mật khẩu"
        })
    }

    res.json({
        ok:true,
        message:"Login success",
        data:user
    })

})


// ================= CHANGE PASSWORD (NEW) =================
app.post("/change-password", async (req, res) => {

    try {

        const username = req.body.username
        const newPassword = req.body.newPassword

        console.log("DATA RECEIVED:", req.body)

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

        user.password = newPassword
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


app.listen(3000, () => {
    console.log("Server running on port 3000")
})