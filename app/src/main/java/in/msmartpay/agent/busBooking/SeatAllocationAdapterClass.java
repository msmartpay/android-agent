package in.msmartpay.agent.busBooking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import in.msmartpay.agent.R;

public class SeatAllocationAdapterClass extends BaseAdapter {

        private Context context;
        private LayoutInflater layoutInflater;
        private ArrayList<Item> dataFull = new ArrayList<Item>();

        SeatAllocationAdapterClass(Context context, ArrayList<Item> data) {

            this.context = context;
            dataFull = data;
        }

        @Override
        public int getCount() {
            return dataFull.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;

        }


    class RecordHolder
    {
        public ImageView imageItem;
    }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {

            RecordHolder holder = null;

            if(view == null) {
                layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(R.layout.bus_grid_view_items, parent, false);

                holder = new RecordHolder();
                holder.imageItem = (ImageView) view.findViewById(R.id.seat_booking);
                view.setTag(holder);
            }
            else
            {
                holder = (RecordHolder) view.getTag();
            }


            Item item = dataFull.get(position);
            holder.imageItem.setImageBitmap(item.getImage());

            return view;
        }
    }
