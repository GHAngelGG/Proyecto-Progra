# Vista Verde Condominium — Administration System

Desktop application for managing the Vista Verde residential condominium. Built as the final project for **Programming I** at **Universidad Mariano Gálvez de Guatemala**.

The system handles 30 houses, their owners and monthly maintenance fee payments (Q1,500.00 each by default).

---

## Team / Equipo

| Name | Student ID (Carné) | Role |
|---|---|---|
| Jose Angel Gonzalez | 0900-24-8660 | Developer / Project Lead |

---

## Project links

- **GitHub repository:** https://github.com/GHAngelGG/Proyecto-Progra
- **Jira board:** https://joseangmil100.atlassian.net/jira/software/projects/VV/boards

---

## What the system does

The administrator can:

1. **Log in** with username and password
2. **Register property owners** and assign them to one of the 30 houses
3. **Record monthly payments** for any house (with validation)
4. **Configure the monthly fee** amount
5. **View the payment history** of any house (paid and pending months)
6. **Generate a general report** of all 30 houses showing payment status
7. **List delinquent houses** that have not paid the current month

---

## Screenshots

| # | Screen | Image |
|---|---|---|
| 1 | Login | `docs/screenshots/01-login.png` |
| 2 | Main Menu | `docs/screenshots/02-menu.png` |
| 3 | Register Owner | `docs/screenshots/03-register-owner.png` |
| 4 | Register Payment | `docs/screenshots/04-register-payment.png` |
| 5 | Fee Configuration | `docs/screenshots/05-fee-configuration.png` |
| 6 | Account Statement | `docs/screenshots/06-account-statement.png` |
| 7 | General Report | `docs/screenshots/07-general-report.png` |
| 8 | Delinquent Houses | `docs/screenshots/08-delinquent-houses.png` |

---

## Technologies

| Tool | Purpose |
|---|---|
| Java 25 | Main language |
| Java Swing | Desktop UI framework |
| NetBeans IDE | Development environment |
| SQLite | Local persistence (extra) |
| FlatLaf | Modern look-and-feel (extra) |
| JavaMail | Email notifications (extra) |
| Git + GitHub | Version control |
| Jira (Scrum) | Project planning |

---

## How to run

1. Clone the repository or download the ZIP
2. Open **NetBeans IDE** → File → Open Project → select the `login-proyecto` folder
3. Add the JAR libraries (right-click on **Libraries** → Add JAR/Folder) — the JARs are in `login-proyecto/lib/`:
   - `flatlaf-3.4.jar`
   - `sqlite-jdbc-3.45.3.0.jar`
   - `slf4j-api-1.7.36.jar`
   - `javax.mail-1.6.2.jar`
4. Right-click the project → Run (F6)
5. Log in with:
   - **Username:** `iusr_vistaverde`
   - **Password:** `R3sidencial2026%`

---

## Validations

- **Owner name:** only letters, accents, ñ and spaces. Minimum first and last name.
- **Phone:** exactly 8 digits, Guatemala format. Symbols and letters blocked.
- **Email:** must follow `name@domain.com` format (regex validation).
- **Payments:** no future months, no duplicates per month, sequential order required (January must be paid before February).
- **Fee:** positive number greater than zero.

---

## Extra features

- **Persistence with SQLite** — all data is saved to `vistaverde.db` and survives app restarts (+5 pts)
- **FlatLaf UI framework** — modern flat design replaces the default Java look (+2 pts)
- **Email notifications** — owners receive a payment receipt by email when their fee is registered (+2 pts)

---

## Project structure

```
Proyecto-Progra/
├── README.md
├── docs/
│   ├── manual/              ← user manual PDF
│   ├── diagramas/           ← class diagram
│   └── screenshots/         ← screen captures
└── login-proyecto/
    ├── lib/                 ← external JAR libraries
    ├── src/
    │   ├── ui/              ← Java Swing screens
    │   ├── model/           ← data classes
    │   └── logic/           ← database + email + context
    └── nbproject/
```

---

## References

- Oracle Java Swing Tutorial — https://docs.oracle.com/javase/tutorial/uiswing/
- SQLite JDBC documentation — https://github.com/xerial/sqlite-jdbc
- FlatLaf documentation — https://www.formdev.com/flatlaf/
- JavaMail API — https://eclipse-ee4j.github.io/mail/
- Bro Code (Java GUI tutorials) — https://www.youtube.com/@BroCodez
- Fazt Code (Java en español) — https://www.youtube.com/@FaztCode
- Stack Overflow — for specific issues (DocumentFilter, JTable rendering)
- GitHub Docs — branching, pull requests, merge workflow

---
---

# Sistema de Administración — Condominio Vista Verde

Aplicación de escritorio para administrar el Condominio Vista Verde. Desarrollada como proyecto final del curso de **Programación I** en la **Universidad Mariano Gálvez de Guatemala**.

El sistema maneja 30 casas, sus propietarios y los pagos de la cuota mensual de mantenimiento (Q1,500.00 cada una por defecto).

---

## ¿Qué hace el sistema?

El administrador puede:

1. **Iniciar sesión** con usuario y contraseña
2. **Registrar propietarios** y asignarlos a una de las 30 casas
3. **Registrar pagos mensuales** con todas las validaciones
4. **Configurar el monto** de la cuota mensual
5. **Ver el historial de pagos** de cualquier casa (meses pagados y pendientes)
6. **Generar el reporte general** del condominio con el estado actual de las 30 casas
7. **Listar las casas morosas** que no pagaron el mes actual

---

## Cómo ejecutarlo

1. Clona el repositorio o descarga el ZIP
2. Abre **NetBeans IDE** → File → Open Project → selecciona la carpeta `login-proyecto`
3. Agrega los JARs (clic derecho en **Libraries** → Add JAR/Folder) — están en `login-proyecto/lib/`:
   - `flatlaf-3.4.jar`
   - `sqlite-jdbc-3.45.3.0.jar`
   - `slf4j-api-1.7.36.jar`
   - `javax.mail-1.6.2.jar`
4. Clic derecho en el proyecto → Run (F6)
5. Inicia sesión con:
   - **Usuario:** `iusr_vistaverde`
   - **Contraseña:** `R3sidencial2026%`

---

## Validaciones

- **Nombre del propietario:** solo letras, acentos, ñ y espacios. Requiere nombre y apellido.
- **Teléfono:** exactamente 8 dígitos, formato Guatemala. Bloquea letras y símbolos.
- **Correo:** debe seguir el formato `nombre@dominio.com` (validación con regex).
- **Pagos:** no se permiten meses futuros, no duplicados, deben pagarse en orden (enero antes que febrero).
- **Cuota:** debe ser un número positivo mayor a cero.

---

## Funcionalidades extra

- **Persistencia con SQLite** — los datos se guardan en `vistaverde.db` y sobreviven al cerrar la app (+5 pts)
- **Framework FlatLaf** — diseño plano y moderno reemplaza el look por defecto de Java (+2 pts)
- **Notificaciones por correo** — al registrar un pago, el propietario recibe un comprobante por email (+2 pts)

---

## Referencias

- Tutorial oficial de Java Swing — https://docs.oracle.com/javase/tutorial/uiswing/
- Documentación SQLite JDBC — https://github.com/xerial/sqlite-jdbc
- Documentación FlatLaf — https://www.formdev.com/flatlaf/
- API de JavaMail — https://eclipse-ee4j.github.io/mail/
- Bro Code (tutoriales Java GUI) — https://www.youtube.com/@BroCodez
- Fazt Code (Java en español) — https://www.youtube.com/@FaztCode
- Stack Overflow — para temas específicos (DocumentFilter, JTable rendering)
- GitHub Docs — ramas, pull requests, flujo de merge
