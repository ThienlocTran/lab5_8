# Lab 7 - Bài 4: Employee Management Web Client (2 điểm)

## 🎯 Mục Tiêu

Tạo **web interface** cho phép người dùng quản lý nhân viên thông qua giao diện trực quan. Tương tác với **REST API** từ Bài 3 sử dụng **Fetch API**.

---

## 📝 Yêu Cầu

### 1. Tạo HTML File với Form & Table

**File cần tạo**: `src/main/webapp/employee-rest-client.html`

**Requirements:**
- **Form input elements:**
  - Input: `id` (mã nhân viên)
  - Input: `name` (tên nhân viên)
  - Radio buttons: `gender` (Male/Female)
  - Input: `salary` (mức lương)

- **Buttons:**
  - `Create` - Tạo nhân viên mới
  - `Update` - Cập nhật nhân viên hiện tại
  - `Delete` - Xóa nhân viên
  - `Reset` - Xóa trắng form

- **Table:**
  - Columns: Id, Name, Gender, Salary, Action
  - Body: hiển thị danh sách nhân viên
  - Mỗi hàng có link "Edit" để chỉnh sửa

**HTML Structure Cơ Bản:**
```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Employee Management - REST Consumer</title>
</head>
<body>
    <div>
        <input id="id" placeholder="Id?"><br>
        <input id="name" placeholder="Name?"><br>
        <input type="radio" id="male" name="gender" checked> Male
        <input type="radio" id="female" name="gender"> Female<br>
        <input id="salary" placeholder="Salary?"><br>
        <hr>
        <button onclick="ctrl.create()">Create</button>
        <button onclick="ctrl.update()">Update</button>
        <button onclick="ctrl.delete()">Delete</button>
        <button onclick="ctrl.reset()">Reset</button>
    </div>
    <hr>
    <table border="1" style="width: 100%">
        <thead>
            <tr>
                <th>Id</th>
                <th>Name</th>
                <th>Gender</th>
                <th>Salary</th>
                <th></th>
            </tr>
        </thead>
        <tbody id="list"></tbody>
    </table>
    
    <script>
        var ctrl = {
            // TODO: Implement methods here
        }
        ctrl.loadAll();
    </script>
</body>
</html>
```

---

### 2. Implement JavaScript Controller

**Requirements:**
Implement một `ctrl` object với các methods:

| Method | Purpose |
|--------|---------|
| `setForm(employee)` | Điền form từ employee object |
| `getForm()` | Lấy data từ form thành employee object |
| `fillToTable(employees)` | Hiển thị array employees trong table |
| `loadAll()` | GET /employees, hiển thị danh sách |
| `create()` | POST /employees, tạo mới |
| `update()` | PUT /employees/{id}, cập nhật |
| `delete()` | DELETE /employees/{id}, xóa |
| `reset()` | Xóa trắng form |
| `edit(id)` | GET /employees/{id}, fill form |

---

## 🔧 Step-by-Step Hướng Dẫn

### Bước 1: Tạo HTML Structure

1. **Tạo file** `employee-rest-client.html`

2. **Tạo form section:**
   ```html
   <div>
       <input id="id" placeholder="Id?"><br>
       <input id="name" placeholder="Name?"><br>
       <input type="radio" id="male" name="gender" checked> Male
       <input type="radio" id="female" name="gender"> Female<br>
       <input id="salary" placeholder="Salary?"><br>
       <hr>
       <button onclick="ctrl.create()">Create</button>
       <button onclick="ctrl.update()">Update</button>
       <button onclick="ctrl.delete()">Delete</button>
       <button onclick="ctrl.reset()">Reset</button>
   </div>
   ```

3. **Tạo table section:**
   ```html
   <table border="1" style="width: 100%">
       <thead>
           <tr>
               <th>Id</th>
               <th>Name</th>
               <th>Gender</th>
               <th>Salary</th>
               <th></th>
           </tr>
       </thead>
       <tbody id="list"></tbody>
   </table>
   ```

---

### Bước 2: Implement JavaScript Methods

#### 1. `setForm(employee)` - Điền form từ object
```javascript
setForm(employee) {
    document.getElementById("id").value = employee.id || "";
    document.getElementById("name").value = employee.name || "";
    document.getElementById("salary").value = employee.salary || "";
    if(employee.gender) {
        document.getElementById("male").checked = true;
    } else {
        document.getElementById("female").checked = true;
    }
}
```

**Hint:** Set value cho input elements, check radio button đúng

---

#### 2. `getForm()` - Lấy data từ form
```javascript
getForm() {
    return {
        id: document.getElementById("id").value,
        name: document.getElementById("name").value,
        gender: document.getElementById("male").checked,
        salary: parseFloat(document.getElementById("salary").value)
    }
}
```

**Hint:** Lấy .value từ input, checked từ radio, convert salary thành float

---

#### 3. `fillToTable(employees)` - Hiển thị danh sách
```javascript
fillToTable(employees) {
    var rows = [];
    employees.forEach(e => {
        var row = `<tr>
            <td>${e.id}</td>
            <td>${e.name}</td>
            <td>${e.gender?'Male':'Female'}</td>
            <td>${e.salary}</td>
            <td><a onclick="ctrl.edit('${e.id}')">Edit</a></td>
        </tr>`;
        rows.push(row);
    });
    document.getElementById("list").innerHTML = rows.join('');
}
```

**Hint:** 
- Loop qua employees array
- Tạo `<tr>` cho mỗi employee
- Dùng ternary operator cho gender: `e.gender?'Male':'Female'`
- Join tất cả rows và set innerHTML

---

#### 4. `loadAll()` - Lấy & hiển thị danh sách
```javascript
loadAll() {
    var url = "http://localhost:8080/employees";
    fetch(url, {method: "GET"})
        .then(resp => resp.json())
        .then(employees => {
            this.fillToTable(employees);
        })
        .catch(error => console.error("Error:", error));
}
```

**Hint:** 
- GET request đến REST API
- Parse JSON response
- Gọi fillToTable() để hiển thị

---

#### 5. `create()` - Tạo nhân viên mới
```javascript
create() {
    var data = this.getForm();
    var url = "http://localhost:8080/employees";
    fetch(url, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(data)
    })
    .then(resp => resp.json())
    .then(json => {
        this.loadAll();
        this.reset();
    })
    .catch(error => console.error("Error:", error));
}
```

**Hint:**
- Lấy form data
- POST request với JSON body
- Sau khi success: reload list, clear form

---

#### 6. `update()` - Cập nhật nhân viên
```javascript
update() {
    var data = this.getForm();
    var url = `http://localhost:8080/employees/${data.id}`;
    fetch(url, {
        method: "PUT",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(data)
    })
    .then(resp => resp.json())
    .then(json => {
        this.loadAll();
        this.reset();
    })
    .catch(error => console.error("Error:", error));
}
```

**Hint:**
- Lấy form data (có ID)
- PUT request với ID trong URL
- Sau khi success: reload list, clear form

---

#### 7. `delete()` - Xóa nhân viên
```javascript
delete() {
    var id = document.getElementById("id").value;
    if(!id) {
        alert("Please select an employee to delete");
        return;
    }
    var url = `http://localhost:8080/employees/${id}`;
    fetch(url, {method: "DELETE"})
        .then(resp => resp.json())
        .then(json => {
            this.loadAll();
            this.reset();
        })
        .catch(error => console.error("Error:", error));
}
```

**Hint:**
- Validate có ID trong form
- DELETE request (no body)
- Reload list & clear form

---

#### 8. `reset()` - Xóa trắng form
```javascript
reset() {
    var employee = {id:"", name:"", salary:0, gender:true};
    this.setForm(employee);
}
```

**Hint:** Tạo empty employee object, gọi setForm()

---

#### 9. `edit(id)` - Load nhân viên để edit
```javascript
edit(id) {
    var url = `http://localhost:8080/employees/${id}`;
    fetch(url, {method: "GET"})
        .then(resp => resp.json())
        .then(employee => {
            this.setForm(employee);
        })
        .catch(error => console.error("Error:", error));
}
```

**Hint:**
- GET request với ID
- Parse response
- Gọi setForm() để fill form

---

### Bước 3: Initialize Page

```javascript
// Ở cuối script, load data khi page load
ctrl.loadAll();
```

---

## 🧪 Cách Test

1. **Build project**:
   ```bash
   mvn clean package
   ```

2. **Deploy** `ROOT.war` lên Tomcat

3. **Start Tomcat**

4. **Mở browser** vào: `http://localhost:8080/employee-rest-client.html`

5. **Kiểm tra initial load:**
   - Table phải show 5 nhân viên (NV01-NV05)
   - Form phải trống

---

### Test Kịch Bản

**Kịch bản 1: View & Edit**
```
1. Page load → Table show 5 employees
2. Click "Edit" trên row NV03
3. Form fill với: NV03, Nhân viên 03, Male, 5000
✓ Pass
```

**Kịch bản 2: Create**
```
1. Click "Reset"
2. Nhập: ID=NV06, Name=Test, Gender=Male, Salary=5000
3. Click "Create"
4. Table update: thêm NV06
5. Form clear
✓ Pass
```

**Kịch bản 3: Update**
```
1. Click "Edit" trên NV06
2. Change name → "Updated Name"
3. Click "Update"
4. Table update name của NV06
5. Form clear
✓ Pass
```

**Kịch bản 4: Delete**
```
1. Click "Edit" trên NV06
2. Click "Delete"
3. Table update: NV06 disappear
4. Form clear
5. List back to 5 employees
✓ Pass
```

---

## 📚 Kiến Thức Cần Biết

### DOM Manipulation
```javascript
document.getElementById("id").value      // Get value
document.getElementById("id").value = "" // Set value
element.innerHTML = html                 // Set HTML content
element.innerHTML = rows.join('')        // Join array to string
```

### JavaScript ES6 Template Literals
```javascript
// Using backticks:
var url = `http://localhost:8080/employees/${id}`;
var html = `<tr><td>${e.id}</td></tr>`;
```

### Arrow Functions & Fat This
```javascript
// Arrow functions inherit 'this' from context
.then(data => {
    this.fillToTable(data);  // 'this' refers to ctrl object
})

// vs regular function (wrong):
.then(function(data) {
    this.fillToTable(data);  // 'this' is undefined
})
```

### JSON.stringify
```javascript
var data = {id: "NV01", name: "Test"};
var json = JSON.stringify(data);
// Result: '{"id":"NV01","name":"Test"}'
```

---

## 💡 Tips & Tricks

1. **Ternary Operator for Gender:**
   ```javascript
   e.gender ? 'Male' : 'Female'
   ```

2. **Check Empty String:**
   ```javascript
   if(!id || id.trim() === "") {
       alert("ID is required");
   }
   ```

3. **Array forEach:**
   ```javascript
   array.forEach(item => {
       console.log(item);
   });
   ```

4. **Debugging with Console:**
   ```javascript
   console.log("Form data:", this.getForm());
   console.log("Employees:", employees);
   ```

5. **Network Debugging:**
   - F12 → Network tab
   - Perform action
   - Click request → Response tab
   - Verify JSON response

---

## ❌ Lỗi Thường Gặp

| Lỗi | Nguyên Nhân | Giải Pháp |
|-----|-------------|----------|
| Form không fill | setForm() sai | Check element ID matching |
| Table blank | fillToTable() empty array | Verify loadAll() working |
| Create fail | JSON format sai | Log form data, validate |
| Edit không work | onclick parameter sai | Check single quotes trong onclick |
| Form không clear | reset() chưa call | Call reset() sau action |
| 'this' undefined | Arrow function wrong | Use arrow functions everywhere |

---

## 🔍 Debugging Tips

1. **Log form data before send:**
   ```javascript
   var data = this.getForm();
   console.log("Sending:", data);
   ```

2. **Log response:**
   ```javascript
   .then(resp => {
       console.log("Response:", resp);
       return resp.json();
   })
   ```

3. **Check API responses:**
   - F12 → Network tab
   - Click request
   - Check Response tab

4. **Validate JSON:**
   - Copy response JSON
   - Paste vào [jsonlint.com](https://jsonlint.com)

---

## 🎓 Learning Outcomes

Sau bài này, bạn sẽ biết:
- ✅ Tạo dynamic HTML từ JavaScript
- ✅ Gọi REST API từ browser
- ✅ DOM manipulation & event handling
- ✅ Parse & format JSON
- ✅ CRUD operations từ web UI
- ✅ Error handling & debugging
- ✅ ES6 features (arrow functions, template literals)

---

## ✨ Bonus (Optional)

Sau khi hoàn thành, bạn có thể thử:
- Add CSS styling (make it look beautiful)
- Add input validation
- Add loading indicator
- Add error messages display
- Add search/filter functionality
- Paginate large lists
- Add sort by column
- Add confirmation dialog before delete
- Responsive design for mobile

---

**Status**: Ready for Implementation
**Points**: 2 điểm
**Time Estimate**: 40-50 phút (nếu quen với JS) hoặc 60+ phút (nếu mới học)
