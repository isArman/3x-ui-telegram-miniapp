import React from 'react'

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label?: string
  error?: string
}

export const Input: React.FC<InputProps> = ({ label, error, className = '', ...props }) => {
  const inputStyles = 'w-full px-3 py-2 rounded-lg border border-telegram-text/20 bg-telegram-bg text-telegram-text focus:outline-none focus:ring-2 focus:ring-telegram-link transition-colors'

  return (
    <div className={className}>
      {label && <label className="block text-sm font-medium mb-1">{label}</label>}
      <input className={inputStyles} {...props} />
      {error && <p className="mt-1 text-sm text-red-500">{error}</p>}
    </div>
  )
}
