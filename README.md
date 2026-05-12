# Vista Verde Condominium — Administration System

A desktop application for managing a residential condominium, built with Java Swing as the final project for the Programming I course at Universidad Mariano Gálvez de Guatemala.

---

## What it does

The system lets the condominium administrator:

- Log in with a username and password
- Register property owners and assign them to one of the 30 houses
- Record monthly fee payments for each house
- Configure the monthly fee amount
- View the full payment history for any house
- Generate a monthly income report
- See which houses have pending/overdue fees

---

## Technologies

| Tool | Purpose |
|---|---|
| Java 21 | Main programming language |
| Java Swing | Graphical user interface |
| NetBeans IDE | Development environment |
| Git + GitHub | Version control |
| Jira | Project planning (Scrum) |

---

## Project structure

```
src/
├── login/proyecto/
│   ├── LoginProyecto.java      ← entry point (main)
│   ├── Inicio.java             ← login screen
│   ├── Menu.java               ← main menu
│   ├── RegistroPropietario.java ← register owner
│   ├── RegisterPayment.java    ← register payments
│   ├── FeeConfiguration.java  ← configure fee
│   ├── AccountStatement.java  ← payment history
│   ├── GeneralReport.java     ← income report
│   └── DelinquentHouses.java  ← overdue houses
└── vistaverde/
    ├── AppContext.java         ← shared data (singleton)
    └── model/
        ├── Condominio.java
        ├── Casa.java
        ├── Propietario.java
        └── Pago.java
```

---

## How to run

1. Open NetBeans IDE
2. File → Open Project → select the `login-proyecto` folder
3. Right-click the project → Run (or press F6)
4. Log in with: **username:** `iusr_vistaverde` / **password:** `admin123`

---

## Validations

- Phone number: Guatemala format, exactly 8 digits, only numbers allowed
- Email: must follow a valid format (name@domain.com)
- Payments: cannot register future months, no duplicates, must follow sequential order (January before February, etc.)
- Fee: must be a positive number greater than zero

---

## References

These are the main resources I used while building this project:

- Oracle Java Swing tutorial — https://docs.oracle.com/javase/tutorial/uiswing/
- Stack Overflow (layout managers, JTable, DocumentFilter) — https://stackoverflow.com
- Bro Code — Java GUI tutorials on YouTube — https://www.youtube.com/@BroCodez
- Fazt Code — Java en español — https://www.youtube.com/@FaztCode
- GitHub Docs — branching and pull requests — https://docs.github.com

---
---

# Sistema de Administración — Condominio Vista Verde

Aplicación de escritorio para administrar un condominio residencial, desarrollada en Java Swing como proyecto final del curso de Programación I en la Universidad Mariano Gálvez de Guatemala.

---

## ¿Qué hace?

El sistema permite al administrador del condominio:

- Iniciar sesión con usuario y contraseña
- Registrar propietarios y asignarlos a una de las 30 casas
- Registrar el pago mensual de cada casa
- Configurar el monto de la cuota mensual
- Ver el historial completo de pagos por casa
- Generar un reporte mensual de ingresos
- Consultar qué casas tienen pagos pendientes o en mora

---

## Tecnologías utilizadas

| Herramienta | Para qué se usó |
|---|---|
| Java 21 | Lenguaje principal |
| Java Swing | Interfaz gráfica de escritorio |
| NetBeans IDE | Entorno de desarrollo |
| Git + GitHub | Control de versiones |
| Jira | Planificación del proyecto (Scrum) |

---

## Cómo ejecutarlo

1. Abrir NetBeans IDE
2. File → Open Project → seleccionar la carpeta `login-proyecto`
3. Clic derecho en el proyecto → Run (o F6)
4. Iniciar sesión con: **usuario:** `iusr_vistaverde` / **contraseña:** `admin123`

---

## Validaciones implementadas

- Teléfono: formato Guatemala, exactamente 8 dígitos, solo números
- Correo: debe tener formato válido (nombre@dominio.com)
- Pagos: no se permiten meses futuros, no se permiten duplicados, deben registrarse en orden (enero antes que febrero, etc.)
- Cuota: debe ser un número positivo mayor que cero

---

## Referencias utilizadas

- Tutorial oficial de Java Swing — https://docs.oracle.com/javase/tutorial/uiswing/
- Stack Overflow (layouts, JTable, DocumentFilter) — https://stackoverflow.com
- Bro Code — tutoriales de Java GUI en YouTube — https://www.youtube.com/@BroCodez
- Fazt Code — Java en español — https://www.youtube.com/@FaztCode
- GitHub Docs — ramas y pull requests — https://docs.github.com
