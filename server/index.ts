import * as admin from 'firebase-admin'; 
import express = require('express'); 

admin.initializeApp({ credential: admin.credential.applicationDefault(), }); 
const db = admin.firestore() 
const port = 8081 
const app = express() 
const messaging = admin.messaging(); 

async function fetchBookData(bookId) { 
    let bookData;
    try {
        const ref = await db.collection('books').doc(bookId).get()
        bookData = ref.data()
        bookData.bookId = ref.id
        bookData.title = bookData.title || 'Unknown Book'
       
    } catch (e) {
        bookData = {
            title: 'Unknown Book'
        } 
    }
    return bookData
}

async function fetchRequestData(requestId) {
    let rData;
    try {
        const ref = await db.collection('requests').doc(requestId).get()
        rData = ref.data()
        rData.bookId = rData.bookId || ''
        rData.requestId = ref.id
    } catch (e) {
        // pass
        rData.bookId = rData.bookId || ''
        rData.requestId = ''
    }

    return rData
}

app.get('/request/:borrower/:bookId', async (req, res) => {
    const book = await fetchBookData(req.params.bookId)
    const owner = book.owner
    const borrower = req.params.borrower

    const msg = {
          topic: owner,
          notification: {
              title: `${borrower} requested your book <${book.title}>`,
              body: "click to see the book detail",
          },
        data: {
            bookId: book.bookId
        }
      }
    // console.log("sending message: ", msg)
    const messageRes = await messaging.send(msg)
    // console.log("sent response: ", messageRes)
    res.send('ok')
})

app.get('/accept/:requestId', async (req, res) => {
    const request = await fetchRequestData(req.params.requestId)
    const book = await fetchBookData(request.bookId || '')
    const owner = book.owner
    const borrower = request.requester

    const msg = {
          topic: borrower,
          notification: {
              title: `${owner} accepted your request for <${book.title}>`,
              body: "click to see the requested book detail",
          },
        data: {
            bookId: request.bookId,
            requestId: request.requestId
        }
      }
    const msgRes = await messaging.send(msg)
    res.send('ok')
})

app.listen(port, () => {
    // const ref = await db.collection('books').doc('8Mf2bIeS6PaQaxIPo103').get();
    // console.log(ref.id, '=>', ref.data())
    console.log(`listening at http://localhost:${port}`)
})
