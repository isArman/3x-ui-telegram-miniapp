import React from 'react'
import { Link } from 'react-router-dom'

export const NotFoundPage: React.FC = () => {
  return (
    <div className="min-h-screen flex flex-col items-center justify-center p-6 text-center">
      <h1 className="text-4xl font-bold mb-4">Page Not Found</h1>
      <p className="text-xl text-telegram-hint">
        The page you're looking for doesn't exist. Try checking the URL or going back to the home page.
      </p>
      <Link to="/" className="mt-4 inline-block bg-telegram-button text-telegram-button-text px-6 py-3 rounded-lg">
        Go to Home
      </Link>
    </div>
  )
}