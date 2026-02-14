import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar';
import ProductsPage from './pages/ProductsPage';
import RawMaterialsPage from './pages/RawMaterialsPage';
import AssociationsPage from './pages/AssociationsPage';
import SuggestionsPage from './pages/SuggestionsPage';

export default function App() {
  return (
    <BrowserRouter>
      <Navbar />
      <Routes>
        <Route path="/" element={<Navigate to="/products" replace />} />
        <Route path="/products" element={<ProductsPage />} />
        <Route path="/raw-materials" element={<RawMaterialsPage />} />
        <Route path="/associations" element={<AssociationsPage />} />
        <Route path="/suggestions" element={<SuggestionsPage />} />
      </Routes>
    </BrowserRouter>
  );
}
