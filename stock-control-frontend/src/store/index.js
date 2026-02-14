import { configureStore } from '@reduxjs/toolkit';
import productReducer from './productSlice';
import rawMaterialReducer from './rawMaterialSlice';
import productRawMaterialReducer from './productRawMaterialSlice';
import suggestionReducer from './suggestionSlice';

const store = configureStore({
  reducer: {
    products: productReducer,
    rawMaterials: rawMaterialReducer,
    productRawMaterials: productRawMaterialReducer,
    suggestions: suggestionReducer,
  },
});

export default store;
