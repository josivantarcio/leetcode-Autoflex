import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import api from '../api/axios';

export const fetchSuggestions = createAsyncThunk('suggestions/fetch', async () => {
  const res = await api.get('/production-suggestions');
  return res.data;
});

const suggestionSlice = createSlice({
  name: 'suggestions',
  initialState: { data: null, status: 'idle', error: null },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchSuggestions.pending, (state) => { state.status = 'loading'; })
      .addCase(fetchSuggestions.fulfilled, (state, action) => {
        state.status = 'succeeded';
        state.data = action.payload;
      })
      .addCase(fetchSuggestions.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message;
      });
  },
});

export default suggestionSlice.reducer;
