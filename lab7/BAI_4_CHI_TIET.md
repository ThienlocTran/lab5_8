# Lab 7 - Bài 4: Employee Management Web Client (Chi Tiết)

## 📖 Khái Niệm

### Web Client là gì?

**Web Client** = HTML + JavaScript UI để người dùng tương tác với API

**Quy trình:**
1. User nhìn thấy form & table
2. User điền data/click button
3. JavaScript gọi fetch()
4. Gửi request đến REST API (từ Bài 3)
5. API trả JSON response
6. JavaScript update UI (form/table)
7. User thấy kết quả

### Controller Pattern

**Controller** = JavaScript object quản lý tất cả UI interactions

```javascript
var ctrl = {
    setForm(employee) { ... },
    getForm() { ... },
    fillToTable(employees) { ... },
    loadAll() { ... },
    create() { ... },
    update() { ... },
    delete() { ... },
    reset() { ... },
    edit(id) { ... }
}
```

**Lợi ích:**
- Organized code
- Easy to maintain
- Reusable methods

### DOM (Document Object Model)

**DOM** = Tree structure của HTML elements

**Cách access:**
```javascript
// Get element by ID
const input = document.getElementById("id");

// Get value
const value = input.value;

// Set value
input.value = "NV01";

// Get multiple by name
const radios = document.getElementsByName("gender");
```

### Event Handlers

**Event** = User action (click, submit, change, etc.)

```html
<button onclick="ctrl.create()">Create</button>
```

Khi user click button:
1. `onclick` event triggered
2. Call `ctrl.create()` function
3. Function logic executes

---

## 🔄 Bài 4 Quy Trình

### Full CRUD Flow

```
LOAD PAGE
    ↓
1. ctrl.loadAll() called
    ↓
    fetch GET /employees
    ↓
    Parse JSON (array)
    ↓
    fillToTable() - display in table

USER ACTIONS:

CREATE
    ↓
1. getForm() - read from input
2. Fetch POST /employees (body: JSON)
3. loadAll() - refresh
4. reset() - clear form

EDIT
    ↓
1. Click "Edit" link in table row
2. edit(id) - fetch GET /employees/{id}
3. setForm() - fill input from response

UPDATE
    ↓
1. getForm() - read from input
2. Fetch PUT /employees/{id} (body: JSON)
3. loadAll() - refresh
4. reset() - clear form

DELETE
    ↓
1. getForm() - read ID from input
2. Fetch DELETE /employees/{id}
3. loadAll() - refresh
4. reset() - clear form

RESET
    ↓
1. setForm() - clear all inputs
```

---

## 💻 PHẦN 1: HTML STRUCTURE

### Input Elements

```html
<input id="id" placeholder="Id?"><br>
<input id="name" placeholder="Name?"><br>
<input type="radio" id="male" name="gender" checked> Male
<input type="radio" id="female" name="gender"> Female<br>
<input id="salary" placeholder="Salary?"><br>
```

**Giải thích:**
- `id="id"` - Access via `document.getElementById("id")`
- `placeholder` - Hint text
- `type="radio"` - Radio button (mutually exclusive)
- `name="gender"` - Group name (only one can be selected)
- `checked` - Default selected

### Buttons

```html
<button onclick="ctrl.create()">Create</button>
<button onclick="ctrl.update()">Update</button>
<button onclick="ctrl.delete()">Delete</button>
<button onclick="ctrl.reset()">Reset</button>
```

**Giải thích:**
- `onclick="ctrl.create()"` - Call method when clicked
- Browser automatically calls function

### Table Structure

```html
<table border="1">
    <thead>
        <tr>
            <th>Id</th>
            <th>Name</th>
            <th>Gender</th>
            <th>Salary</th>
            <th></th>  <!-- Edit column -->
        </tr>
    </thead>
    <tbody id="list"></tbody>
</table>
```

**Giải th�ch:**
- `<thead>` - Table header (fixed)
- `<tbody id="list">` - Table body (dynamic - filled by JS)
- `id="list"` - Access via JavaScript to update

### HTML Template

**File path:** `src/main/webapp/employee-rest-client.html`

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Employee Management - REST Consumer</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .form-section {
            margin-bottom: 20px;
            padding: 15px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        input[type="text"] {
            width: 200px;
            padding: 8px;
            margin: 5px 0;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        button {
            padding: 8px 15px;
            margin: 5px;
            cursor: pointer;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
        }
        button:hover {
            background-color: #0056b3;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 12px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        tr:hover {
            background-color: #f9f9f9;
        }
        a {
            color: #007bff;
            cursor: pointer;
            text-decoration: underline;
        }
        a:hover {
            color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Employee Management - REST API Client</h1>
        
        <!-- Form Section -->
        <div class="form-section">
            <h2>Employee Form</h2>
            <div>
                <input id="id" placeholder="Id?" type="text"><br>
                <input id="name" placeholder="Name?" type="text"><br>
                <div>
                    <input type="radio" id="male" name="gender" checked> Male
                    <input type="radio" id="female" name="gender"> Female
                </div>
                <br>
                <input id="salary" placeholder="Salary?" type="text"><br>
                <hr>
                <button onclick="ctrl.create()">Create</button>
                <button onclick="ctrl.update()">Update</button>
                <button onclick="ctrl.delete()">Delete</button>
                <button onclick="ctrl.reset()">Reset</button>
            </div>
        </div>
        
        <!-- Table Section -->
        <h2>Employee List</h2>
        <table border="1">
            <thead>
                <tr>
                    <th>Id</th>
                    <th>Name</th>
                    <th>Gender</th>
                    <th>Salary</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody id="list"></tbody>
        </table>
    </div>
    
    <script>
        var ctrl = {
            // TODO: Implement 9 methods here
        }
        
        // Initialize: load all employees when page loads
        ctrl.loadAll();
    </script>
</body>
</html>
```

---

## 💻 PHẦN 2: JAVASCRIPT CONTROLLER METHODS

### Method 1: setForm(employee)

**Purpose:** Fill form inputs from employee object

**When used:**
- After clicking "Edit" in table
- After clicking "Reset" button

**Implementation:**

```javascript
setForm(employee) {
    // Set text inputs
    document.getElementById("id").value = employee.id || "";
    document.getElementById("name").value = employee.name || "";
    document.getElementById("salary").value = employee.salary || "";
    
    // Set radio button
    if(employee.gender) {
        document.getElementById("male").checked = true;
    } else {
        document.getElementById("female").checked = true;
    }
}
```

**Giải thích:**
- `document.getElementById()` - Get element by ID
- `.value = ` - Set input value
- `||` - Default to "" if undefined
- `.checked = true` - Check radio button
- Ternary operator: `employee.gender ? male : female`

**Ví dụ:**
```javascript
var emp = {id: "NV01", name: "Tèo", gender: true, salary: 5000};
ctrl.setForm(emp);
// Result: form fills with NV01, Tèo, Male, 5000
```

---

### Method 2: getForm()

**Purpose:** Create object from form inputs

**When used:**
- Before calling create(), update(), delete()

**Implementation:**

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

**Giải thích:**
- `.value` - Get input value
- `.checked` - Get radio button state (true/false)
- `parseFloat()` - Convert string to number

**Ví dụ:**
```javascript
// User filled form:
// ID: NV06
// Name: New Employee
// Gender: Female (checked)
// Salary: 9000

var data = ctrl.getForm();
// Result: {id: "NV06", name: "New Employee", gender: false, salary: 9000}
```

---

### Method 3: fillToTable(employees)

**Purpose:** Display employee array in table

**When used:**
- After loadAll() gets response
- After create/update/delete refreshes data

**Implementation:**

```javascript
fillToTable(employees) {
    var rows = [];
    
    // Loop through employees
    employees.forEach(e => {
        var genderText = e.gender ? 'Male' : 'Female';
        
        var row = `<tr>
            <td>${e.id}</td>
            <td>${e.name}</td>
            <td>${genderText}</td>
            <td>${e.salary}</td>
            <td><a onclick="ctrl.edit('${e.id}')">Edit</a></td>
        </tr>`;
        
        rows.push(row);
    });
    
    // Set table body HTML
    document.getElementById("list").innerHTML = rows.join('');
}
```

**Giải thích:**
- `employees.forEach()` - Loop through array
- Template literal: `` `<tr>${e.id}</tr>` ``
- `${e.id}` - Embed variable in string
- `e.gender ? 'Male' : 'Female'` - Ternary operator
- `rows.push()` - Add to array
- `rows.join('')` - Join all rows
- `innerHTML = ` - Replace table body content

**Ví dụ:**
```javascript
var emps = [
    {id: "NV01", name: "Tèo", gender: true, salary: 500},
    {id: "NV02", name: "Tý", gender: false, salary: 1500}
];
ctrl.fillToTable(emps);
// Result: Table displays 2 rows with Edit links
```

---

### Method 4: loadAll()

**Purpose:** Fetch all employees from API & display

**When used:**
- Page loads (ctrl.loadAll() at end)
- After create/update/delete success

**Implementation:**

```javascript
loadAll() {
    var url = "http://localhost:8080/employees";
    
    fetch(url, {method: "GET"})
        .then(resp => resp.json())
        .then(employees => {
            console.log("Employees loaded:", employees);
            this.fillToTable(employees);
        })
        .catch(error => {
            console.error("Error loading employees:", error);
        });
}
```

**Giải th�ch:**
- `fetch()` - GET request to API
- `.then(resp => resp.json())` - Parse JSON
- `.then(employees => ...)` - Use response
- `this.fillToTable()` - Call method in same object
- `.catch()` - Error handling

**Flow:**
```
1. Fetch GET /employees
2. Parse response JSON
3. Call fillToTable(employees) to display
```

---

### Method 5: create()

**Purpose:** Create new employee via API

**When used:**
- User click "Create" button

**Implementation:**

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
        console.log("Employee created:", json);
        this.loadAll();      // Refresh table
        this.reset();        // Clear form
    })
    .catch(error => console.error("Create error:", error));
}
```

**Giải thích:**
- `getForm()` - Get data from inputs
- `method: "POST"` - HTTP POST
- `JSON.stringify(data)` - Convert object to JSON string
- After success: `loadAll()` to refresh, `reset()` to clear

**Flow:**
```
1. getForm() → {id: "NV06", name: "Test", gender: false, salary: 9000}
2. Fetch POST /employees with body
3. API creates employee
4. loadAll() - refresh table with new employee
5. reset() - clear form inputs
```

---

### Method 6: update()

**Purpose:** Update existing employee via API

**When used:**
- User click "Update" button

**Implementation:**

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
        console.log("Employee updated");
        this.loadAll();      // Refresh table
        this.reset();        // Clear form
    })
    .catch(error => console.error("Update error:", error));
}
```

**Giải thích:**
- `data.id` - Get ID from form
- `` `http://.../employees/${data.id}` `` - Template literal
- `method: "PUT"` - HTTP PUT (update)
- Rest similar to create()

**Flow:**
```
1. getForm() → {id: "NV06", ...}
2. URL becomes: /employees/NV06
3. Fetch PUT with body
4. API updates employee NV06
5. loadAll() - refresh
6. reset() - clear
```

---

### Method 7: delete()

**Purpose:** Delete employee via API

**When used:**
- User click "Delete" button

**Implementation:**

```javascript
delete() {
    var id = document.getElementById("id").value;
    
    // Validation
    if(!id) {
        alert("Please select an employee to delete");
        return;
    }
    
    var url = `http://localhost:8080/employees/${id}`;
    
    fetch(url, {method: "DELETE"})
        .then(resp => resp.json())
        .then(json => {
            console.log("Employee deleted");
            this.loadAll();      // Refresh table
            this.reset();        // Clear form
        })
        .catch(error => console.error("Delete error:", error));
}
```

**Giải thích:**
- Get ID from form
- Validate ID exists (prevent accidental delete)
- `method: "DELETE"` - HTTP DELETE
- No body needed for DELETE
- loadAll() & reset() after success

**Flow:**
```
1. Get ID from input
2. Check if ID is empty
3. Fetch DELETE /employees/{id}
4. API deletes employee
5. loadAll() - refresh (employee should disappear)
6. reset() - clear
```

---

### Method 8: reset()

**Purpose:** Clear all form inputs

**When used:**
- User click "Reset" button
- After create/update/delete success

**Implementation:**

```javascript
reset() {
    var employee = {
        id: "",
        name: "",
        salary: 0,
        gender: true
    };
    this.setForm(employee);
}
```

**Giải thích:**
- Create empty employee object
- Call setForm() to clear inputs
- gender: true → Male checkbox will be checked

**Result:**
```
All inputs cleared:
ID: ""
Name: ""
Gender: Male (checked)
Salary: ""
```

---

### Method 9: edit(id)

**Purpose:** Load employee and fill form (for editing)

**When used:**
- User click "Edit" link in table

**Implementation:**

```javascript
edit(id) {
    var url = `http://localhost:8080/employees/${id}`;
    
    fetch(url, {method: "GET"})
        .then(resp => resp.json())
        .then(employee => {
            console.log("Employee loaded:", employee);
            this.setForm(employee);  // Fill form
        })
        .catch(error => console.error("Edit error:", error));
}
```

**Giải thích:**
- Get ID parameter from function call
- `` `http://.../employees/${id}` `` - URL with ID
- `method: "GET"` - Fetch single employee
- `setForm()` - Fill form with response

**Flow:**
```
1. User click "Edit" link in table (pass ID)
2. Fetch GET /employees/{id}
3. API returns employee object
4. setForm(employee) - fill form inputs
5. User can now modify and click "Update"
```

---

## 💻 PHẦN 3: COMPLETE CODE

### Full HTML + JavaScript

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Employee Management - REST Consumer</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .form-section {
            margin-bottom: 20px;
            padding: 15px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        input[type="text"] {
            width: 200px;
            padding: 8px;
            margin: 5px 0;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        button {
            padding: 8px 15px;
            margin: 5px;
            cursor: pointer;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
        }
        button:hover {
            background-color: #0056b3;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 12px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        tr:hover {
            background-color: #f9f9f9;
        }
        a {
            color: #007bff;
            cursor: pointer;
            text-decoration: underline;
        }
        a:hover {
            color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Employee Management - REST API Client</h1>
        
        <div class="form-section">
            <h2>Employee Form</h2>
            <div>
                <input id="id" placeholder="Id?" type="text"><br>
                <input id="name" placeholder="Name?" type="text"><br>
                <div>
                    <input type="radio" id="male" name="gender" checked> Male
                    <input type="radio" id="female" name="gender"> Female
                </div>
                <br>
                <input id="salary" placeholder="Salary?" type="text"><br>
                <hr>
                <button onclick="ctrl.create()">Create</button>
                <button onclick="ctrl.update()">Update</button>
                <button onclick="ctrl.delete()">Delete</button>
                <button onclick="ctrl.reset()">Reset</button>
            </div>
        </div>
        
        <h2>Employee List</h2>
        <table border="1">
            <thead>
                <tr>
                    <th>Id</th>
                    <th>Name</th>
                    <th>Gender</th>
                    <th>Salary</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody id="list"></tbody>
        </table>
    </div>
    
    <script>
        var ctrl = {
            setForm(employee) {
                document.getElementById("id").value = employee.id || "";
                document.getElementById("name").value = employee.name || "";
                document.getElementById("salary").value = employee.salary || "";
                if(employee.gender) {
                    document.getElementById("male").checked = true;
                } else {
                    document.getElementById("female").checked = true;
                }
            },
            
            getForm() {
                return {
                    id: document.getElementById("id").value,
                    name: document.getElementById("name").value,
                    gender: document.getElementById("male").checked,
                    salary: parseFloat(document.getElementById("salary").value)
                }
            },
            
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
            },
            
            loadAll() {
                var url = "http://localhost:8080/employees";
                fetch(url, {method: "GET"})
                    .then(resp => resp.json())
                    .then(employees => {
                        console.log("Employees loaded:", employees);
                        this.fillToTable(employees);
                    })
                    .catch(error => console.error("Error:", error));
            },
            
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
                    console.log("Created:", json);
                    this.loadAll();
                    this.reset();
                })
                .catch(error => console.error("Error:", error));
            },
            
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
                    console.log("Updated");
                    this.loadAll();
                    this.reset();
                })
                .catch(error => console.error("Error:", error));
            },
            
            delete() {
                var id = document.getElementById("id").value;
                if(!id) {
                    alert("Please select an employee");
                    return;
                }
                var url = `http://localhost:8080/employees/${id}`;
                fetch(url, {method: "DELETE"})
                    .then(resp => resp.json())
                    .then(json => {
                        console.log("Deleted");
                        this.loadAll();
                        this.reset();
                    })
                    .catch(error => console.error("Error:", error));
            },
            
            reset() {
                var employee = {id:"", name:"", salary:0, gender:true};
                this.setForm(employee);
            },
            
            edit(id) {
                var url = `http://localhost:8080/employees/${id}`;
                fetch(url, {method: "GET"})
                    .then(resp => resp.json())
                    .then(employee => {
                        console.log("Editing:", employee);
                        this.setForm(employee);
                    })
                    .catch(error => console.error("Error:", error));
            }
        }
        
        ctrl.loadAll();
    </script>
</body>
</html>
```

---

## 🧪 PHẦN 4: TEST

### Build & Deploy

```bash
mvn clean package
copy target\ROOT.war %TOMCAT_HOME%\webapps\
```

### Test URL

```
http://localhost:8080/employee-rest-client.html
```

---

### Test Scenario 1: Initial Load

**Expected:**
1. Page loads
2. Table shows 5 employees (NV01-NV05)
3. Form is empty

---

### Test Scenario 2: Edit Employee

**Steps:**
1. Click "Edit" link on NV03
2. Form fills: NV03, Nhân viên 03, Male, 5000

---

### Test Scenario 3: Create New

**Steps:**
1. Click "Reset"
2. Fill form:
   - ID: NV06
   - Name: New Employee
   - Gender: Female
   - Salary: 9500
3. Click "Create"

**Expected:**
- Table refreshes
- NV06 added to list
- Form clears

---

### Test Scenario 4: Update

**Steps:**
1. Click "Edit" on NV06
2. Change name to "Updated Name"
3. Click "Update"

**Expected:**
- Table refreshes
- NV06 name updated
- Form clears

---

### Test Scenario 5: Delete

**Steps:**
1. Click "Edit" on NV06
2. Click "Delete"

**Expected:**
- Table refreshes
- NV06 removed
- Back to 5 employees
- Form clears

---

## ✅ CHECKLIST

- [ ] HTML structure complete (form + table)
- [ ] All 9 controller methods implemented
- [ ] Build: `mvn clean package` success
- [ ] Deploy ROOT.war
- [ ] Tomcat started
- [ ] Page loads: shows 5 employees
- [ ] Click "Edit" → form fills
- [ ] Create new → table updates
- [ ] Update → table updates
- [ ] Delete → employee removed
- [ ] Reset → form clears

---

## 📊 SUMMARY

### Methods & Their Flow

```
loadAll()
  ↓ GET /employees
  ↓ Parse JSON array
  ↓ fillToTable()
  
create()
  ↓ getForm()
  ↓ POST /employees
  ↓ loadAll()
  ↓ reset()
  
edit(id)
  ↓ GET /employees/{id}
  ↓ Parse JSON object
  ↓ setForm()
  
update()
  ↓ getForm()
  ↓ PUT /employees/{id}
  ↓ loadAll()
  ↓ reset()
  
delete()
  ↓ getForm() to get ID
  ↓ DELETE /employees/{id}
  ↓ loadAll()
  ↓ reset()
  
reset()
  ↓ setForm() with empty object
```

---

**Congrats! Lab 7 Bài 1-4 Hoàn Thành! 🎉🎉🎉**

All 4 main lessons completed!
Total: 8/10 points
Bài 5: Waiting for teacher specs

Now you understand:
✅ AJAX & Fetch API
✅ HTTP Methods & REST
✅ JSON Serialization
✅ Servlet & Request/Response
✅ DOM Manipulation
✅ Controller Pattern
✅ CRUD Operations
✅ Full-stack Web Development
