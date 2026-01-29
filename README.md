# 📱 Ứng dụng Dò Vé Số Miền Nam (Android)

Ứng dụng Android giúp người dùng **xem kết quả xổ số Miền Nam**,  
**dò 2 số cuối**, giao diện **dễ nhìn – chữ lớn – phù hợp cho người lớn tuổi**.

---

## ✨ Tính năng chính

- 📊 Hiển thị kết quả xổ số Miền Nam theo **từng tỉnh**
- 🔢 Dò **2 số cuối** (tự động tô màu khi trúng)
- 🧓 Giao diện **chữ to, rõ ràng**, dễ nhìn cho người lớn tuổi
- 💾 **Cache kết quả trong ngày**
  - Mở app là **có kết quả ngay**
  - Không cần chờ mạng
- 🔄 Tự động **retry khi server đang khởi động**
- ⏳ Có thông báo “Đang tải dữ liệu…” để người dùng **hiểu & chờ được**

---

## 🛠 Công nghệ sử dụng

- **Android (Java)**
- **Volley** – gọi REST API
- **SharedPreferences** – cache dữ liệu
- **JSON** – xử lý dữ liệu kết quả
- UI thuần Android (LinearLayout, TextView)

---

## 🌐 API sử dụng

```text
GET https://lotteryapi-qo0e.onrender.com/api/xoso/mien-nam
