package com.example.ssgdesking.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ssgdesking.Activity.LoginActivity;
import com.example.ssgdesking.Activity.MainActivity;
import com.example.ssgdesking.Data.ReserveCommentData;
import com.example.ssgdesking.Interface.onBackPressedListener;
import com.example.ssgdesking.R;
import com.example.ssgdesking.View.ReserveAfterDialog;
import com.example.ssgdesking.View.ReserveBeforeDialog;
import com.example.ssgdesking.databinding.FragmentReserveBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReserveFragment extends Fragment implements onBackPressedListener {
    private FragmentReserveBinding binding;
    MainFragment mainFragment;
    private static Context context;
    LinearLayout btnSearch;


    public ReserveFragment() {
        // Required empty public constructor
    }
    private static class SingletonHolder {
        public static final ReserveFragment INSTANCE = new ReserveFragment();
    }

    public static ReserveFragment getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReserveBinding.inflate(inflater);
        context = getContext();

        controller();

        return binding.getRoot();
    }

    private void controller() {
        mainFragment = new MainFragment();

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"검색 기능 클릭",Toast.LENGTH_SHORT).show();
            }
        });

        binding.floor1511.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                binding.floor1511.setBackgroundColor(Color.parseColor("#00FF7F"));
                binding.floor1511.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveAfterDialog dialog = new ReserveAfterDialog(getContext(), "15층 1-1");
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1511.setBackgroundResource(R.drawable.reserve_seat_gray);
                    }
                });
            }
        });

        binding.floor1512.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.floor1512.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveAfterDialog dialog = new ReserveAfterDialog(getContext(), "15층 1-2");
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1512.setBackgroundResource(R.drawable.reserve_seat_gray);
                    }
                });
            }
        });

        binding.floor1513.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.floor1513.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveAfterDialog dialog = new ReserveAfterDialog(getContext(), "15층 1-3");
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1513.setBackgroundResource(R.drawable.reserve_seat_gray);
                    }
                });
            }
        });

        binding.floor1514.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.floor1514.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveAfterDialog dialog = new ReserveAfterDialog(getContext(), "15층 1-4");
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1514.setBackgroundResource(R.drawable.reserve_seat_gray);
                    }
                });
            }
        });

        binding.floor1515.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.floor1515.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveAfterDialog dialog = new ReserveAfterDialog(getContext(), "15층 1-5");
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1515.setBackgroundResource(R.drawable.reserve_seat_gray);
                    }
                });
            }
        });

        binding.floor1516.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.floor1516.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveAfterDialog dialog = new ReserveAfterDialog(getContext(), "15층 1-6");
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1516.setBackgroundResource(R.drawable.reserve_seat_gray);
                    }
                });
            }
        });

        binding.floor1517.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.floor1517.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveAfterDialog dialog = new ReserveAfterDialog(getContext(), "15층 1-7");
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1517.setBackgroundResource(R.drawable.reserve_seat_gray);
                    }
                });
            }
        });

        binding.floor1518.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.floor1518.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveAfterDialog dialog = new ReserveAfterDialog(getContext(), "15층 1-8");
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1518.setBackgroundResource(R.drawable.reserve_seat_gray);
                    }
                });
            }
        });

        binding.floor1519.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.floor1519.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveAfterDialog dialog = new ReserveAfterDialog(getContext(), "15층 1-9");
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1519.setBackgroundResource(R.drawable.reserve_seat_gray);
                    }
                });
            }
        });

        binding.floor1521.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.floor1521.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveAfterDialog dialog = new ReserveAfterDialog(getContext(), "15층 2-1");
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1521.setBackgroundResource(R.drawable.reserve_seat_gray);
                    }
                });
            }
        });

        binding.floor1522.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.floor1522.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveAfterDialog dialog = new ReserveAfterDialog(getContext(), "15층 2-2");
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1522.setBackgroundResource(R.drawable.reserve_seat_gray);
                    }
                });
            }
        });

        binding.floor1523.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 임의의 데이터입니다.
                List<String> listTitle = Arrays.asList("No. 0001", "No. 0002", "No. 0003", "No. 0004");
                List<String> listContent = Arrays.asList(
                        "이 자리 좋아요.",
                        "잠잘수 있는 최고의 자리.",
                        "시몬스 침대.",
                        "이 자리에 앉고 인생이 폈습니다."
                );

                binding.floor1523.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveBeforeDialog dialog = new ReserveBeforeDialog(getContext(), "15층 2-3", addCommentData(listTitle, listContent));
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1523.setBackgroundResource(R.drawable.reserve_seat_red);
                    }
                });
            }
        });

        binding.floor1524.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 임의의 데이터입니다.
                List<String> listTitle = Arrays.asList("No. 0001", "No. 0002", "No. 0003", "No. 0004");
                List<String> listContent = Arrays.asList(
                        "이 자리 좋아요.",
                        "잠잘수 있는 최고의 자리.",
                        "시몬스 침대.",
                        "이 자리에 앉고 인생이 폈습니다."
                );

                binding.floor1524.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveBeforeDialog dialog = new ReserveBeforeDialog(getContext(), "15층 2-4", addCommentData(listTitle, listContent));
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1524.setBackgroundResource(R.drawable.reserve_seat_red);
                    }
                });
            }
        });

        binding.floor1525.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 임의의 데이터입니다.
                List<String> listTitle = Arrays.asList("No. 0001", "No. 0002", "No. 0003", "No. 0004");
                List<String> listContent = Arrays.asList(
                        "이 자리 좋아요.",
                        "잠잘수 있는 최고의 자리.",
                        "시몬스 침대.",
                        "이 자리에 앉고 인생이 폈습니다."
                );

                binding.floor1525.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveBeforeDialog dialog = new ReserveBeforeDialog(getContext(), "15층 2-5", addCommentData(listTitle, listContent));
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1525.setBackgroundResource(R.drawable.reserve_seat_red);
                    }
                });
            }
        });

        binding.floor1526.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 임의의 데이터입니다.
                List<String> listTitle = Arrays.asList("No. 0001", "No. 0002", "No. 0003", "No. 0004");
                List<String> listContent = Arrays.asList(
                        "이 자리 좋아요.",
                        "잠잘수 있는 최고의 자리.",
                        "시몬스 침대.",
                        "이 자리에 앉고 인생이 폈습니다."
                );

                binding.floor1526.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveBeforeDialog dialog = new ReserveBeforeDialog(getContext(), "15층 2-6", addCommentData(listTitle, listContent));
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1526.setBackgroundResource(R.drawable.reserve_seat_red);
                    }
                });
            }
        });

        binding.floor1527.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 임의의 데이터입니다.
                List<String> listTitle = Arrays.asList("No. 0001", "No. 0002", "No. 0003", "No. 0004");
                List<String> listContent = Arrays.asList(
                        "이 자리 좋아요.",
                        "잠잘수 있는 최고의 자리.",
                        "시몬스 침대.",
                        "이 자리에 앉고 인생이 폈습니다."
                );

                binding.floor1527.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveBeforeDialog dialog = new ReserveBeforeDialog(getContext(), "15층 2-7", addCommentData(listTitle, listContent));
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1527.setBackgroundResource(R.drawable.reserve_seat_red);
                    }
                });
            }
        });

        binding.floor1528.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 임의의 데이터입니다.
                List<String> listTitle = Arrays.asList("No. 0001", "No. 0002", "No. 0003", "No. 0004");
                List<String> listContent = Arrays.asList(
                        "이 자리 좋아요.",
                        "잠잘수 있는 최고의 자리.",
                        "시몬스 침대.",
                        "이 자리에 앉고 인생이 폈습니다."
                );

                binding.floor1528.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveBeforeDialog dialog = new ReserveBeforeDialog(getContext(), "15층 2-8", addCommentData(listTitle, listContent));
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1528.setBackgroundResource(R.drawable.reserve_seat_red);
                    }
                });
            }
        });

        binding.floor1529.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 임의의 데이터입니다.
                List<String> listTitle = Arrays.asList("No. 0001", "No. 0002", "No. 0003", "No. 0004");
                List<String> listContent = Arrays.asList(
                        "이 자리 좋아요.",
                        "잠잘수 있는 최고의 자리.",
                        "시몬스 침대.",
                        "이 자리에 앉고 인생이 폈습니다."
                );

                binding.floor1529.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveBeforeDialog dialog = new ReserveBeforeDialog(getContext(), "15층 2-9", addCommentData(listTitle, listContent));
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1529.setBackgroundResource(R.drawable.reserve_seat_red);
                    }
                });
            }
        });

        binding.floor1531.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 임의의 데이터입니다.
                List<String> listTitle = Arrays.asList("No. 0001", "No. 0002", "No. 0003", "No. 0004");
                List<String> listContent = Arrays.asList(
                        "이 자리 좋아요.",
                        "잠잘수 있는 최고의 자리.",
                        "시몬스 침대.",
                        "이 자리에 앉고 인생이 폈습니다."
                );

                binding.floor1531.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveBeforeDialog dialog = new ReserveBeforeDialog(getContext(), "15층 3-1", addCommentData(listTitle, listContent));
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1531.setBackgroundResource(R.drawable.reserve_seat_red);
                    }
                });
            }
        });

        binding.floor1532.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 임의의 데이터입니다.
                List<String> listTitle = Arrays.asList("No. 0001", "No. 0002", "No. 0003", "No. 0004");
                List<String> listContent = Arrays.asList(
                        "이 자리 좋아요.",
                        "잠잘수 있는 최고의 자리.",
                        "시몬스 침대.",
                        "이 자리에 앉고 인생이 폈습니다."
                );

                binding.floor1532.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveBeforeDialog dialog = new ReserveBeforeDialog(getContext(), "15층 3-2", addCommentData(listTitle, listContent));
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1532.setBackgroundResource(R.drawable.reserve_seat_red);
                    }
                });
            }
        });

        binding.floor1533.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 임의의 데이터입니다.
                List<String> listTitle = Arrays.asList("No. 0001", "No. 0002", "No. 0003", "No. 0004");
                List<String> listContent = Arrays.asList(
                        "이 자리 좋아요.",
                        "잠잘수 있는 최고의 자리.",
                        "시몬스 침대.",
                        "이 자리에 앉고 인생이 폈습니다."
                );

                binding.floor1533.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveBeforeDialog dialog = new ReserveBeforeDialog(getContext(), "15층 3-3", addCommentData(listTitle, listContent));
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1533.setBackgroundResource(R.drawable.reserve_seat_red);
                    }
                });
            }
        });

        binding.floor1534.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 임의의 데이터입니다.
                List<String> listTitle = Arrays.asList("No. 0001", "No. 0002", "No. 0003", "No. 0004");
                List<String> listContent = Arrays.asList(
                        "이 자리 좋아요.",
                        "잠잘수 있는 최고의 자리.",
                        "시몬스 침대.",
                        "이 자리에 앉고 인생이 폈습니다."
                );

                binding.floor1534.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveBeforeDialog dialog = new ReserveBeforeDialog(getContext(), "15층 3-4", addCommentData(listTitle, listContent));
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1534.setBackgroundResource(R.drawable.reserve_seat_red);
                    }
                });
            }
        });

        binding.floor1535.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 임의의 데이터입니다.
                List<String> listTitle = Arrays.asList("No. 0001", "No. 0002", "No. 0003", "No. 0004");
                List<String> listContent = Arrays.asList(
                        "이 자리 좋아요.",
                        "잠잘수 있는 최고의 자리.",
                        "시몬스 침대.",
                        "이 자리에 앉고 인생이 폈습니다."
                );

                binding.floor1535.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveBeforeDialog dialog = new ReserveBeforeDialog(getContext(), "15층 3-5", addCommentData(listTitle, listContent));
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1535.setBackgroundResource(R.drawable.reserve_seat_red);
                    }
                });
            }
        });

        binding.floor1536.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 임의의 데이터입니다.
                List<String> listTitle = Arrays.asList("No. 0001", "No. 0002", "No. 0003", "No. 0004");
                List<String> listContent = Arrays.asList(
                        "이 자리 좋아요.",
                        "잠잘수 있는 최고의 자리.",
                        "시몬스 침대.",
                        "이 자리에 앉고 인생이 폈습니다."
                );

                binding.floor1536.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveBeforeDialog dialog = new ReserveBeforeDialog(getContext(), "15층 3-6", addCommentData(listTitle, listContent));
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1536.setBackgroundResource(R.drawable.reserve_seat_red);
                    }
                });
            }
        });

        binding.floor1537.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 임의의 데이터입니다.
                List<String> listTitle = Arrays.asList("No. 0001", "No. 0002", "No. 0003", "No. 0004");
                List<String> listContent = Arrays.asList(
                        "이 자리 좋아요.",
                        "잠잘수 있는 최고의 자리.",
                        "시몬스 침대.",
                        "이 자리에 앉고 인생이 폈습니다."
                );

                binding.floor1537.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveBeforeDialog dialog = new ReserveBeforeDialog(getContext(), "15층 3-7", addCommentData(listTitle, listContent));
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1537.setBackgroundResource(R.drawable.reserve_seat_red);
                    }
                });
            }
        });

        binding.floor1538.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 임의의 데이터입니다.
                List<String> listTitle = Arrays.asList("No. 0001", "No. 0002", "No. 0003", "No. 0004");
                List<String> listContent = Arrays.asList(
                        "이 자리 좋아요.",
                        "잠잘수 있는 최고의 자리.",
                        "시몬스 침대.",
                        "이 자리에 앉고 인생이 폈습니다."
                );

                binding.floor1538.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveBeforeDialog dialog = new ReserveBeforeDialog(getContext(), "15층 3-8", addCommentData(listTitle, listContent));
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1538.setBackgroundResource(R.drawable.reserve_seat_red);
                    }
                });
            }
        });

        binding.floor1539.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 임의의 데이터입니다.
                List<String> listTitle = Arrays.asList("No. 0001", "No. 0002", "No. 0003", "No. 0004");
                List<String> listContent = Arrays.asList(
                        "이 자리 좋아요.",
                        "잠잘수 있는 최고의 자리.",
                        "시몬스 침대.",
                        "이 자리에 앉고 인생이 폈습니다."
                );

                binding.floor1539.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveBeforeDialog dialog = new ReserveBeforeDialog(getContext(), "15층 3-9", addCommentData(listTitle, listContent));
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1539.setBackgroundResource(R.drawable.reserve_seat_red);
                    }
                });
            }
        });

        binding.floor1541.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 임의의 데이터입니다.
                List<String> listTitle = Arrays.asList("No. 0001", "No. 0002", "No. 0003", "No. 0004");
                List<String> listContent = Arrays.asList(
                        "이 자리 좋아요.",
                        "잠잘수 있는 최고의 자리.",
                        "시몬스 침대.",
                        "이 자리에 앉고 인생이 폈습니다."
                );

                binding.floor1541.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveBeforeDialog dialog = new ReserveBeforeDialog(getContext(), "15층 4-1", addCommentData(listTitle, listContent));
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1541.setBackgroundResource(R.drawable.reserve_seat_red);
                    }
                });
            }
        });

        binding.floor1542.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 임의의 데이터입니다.
                List<String> listTitle = Arrays.asList("No. 0001", "No. 0002", "No. 0003", "No. 0004");
                List<String> listContent = Arrays.asList(
                        "이 자리 좋아요.",
                        "잠잘수 있는 최고의 자리.",
                        "시몬스 침대.",
                        "이 자리에 앉고 인생이 폈습니다."
                );

                binding.floor1542.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveBeforeDialog dialog = new ReserveBeforeDialog(getContext(), "15층 4-2", addCommentData(listTitle, listContent));
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1542.setBackgroundResource(R.drawable.reserve_seat_red);
                    }
                });
            }
        });

        binding.floor1543.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 임의의 데이터입니다.
                List<String> listTitle = Arrays.asList("No. 0001", "No. 0002", "No. 0003", "No. 0004");
                List<String> listContent = Arrays.asList(
                        "이 자리 좋아요.",
                        "잠잘수 있는 최고의 자리.",
                        "시몬스 침대.",
                        "이 자리에 앉고 인생이 폈습니다."
                );

                binding.floor1543.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveBeforeDialog dialog = new ReserveBeforeDialog(getContext(), "15층 4-3", addCommentData(listTitle, listContent));
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1543.setBackgroundResource(R.drawable.reserve_seat_red);
                    }
                });
            }
        });

        binding.floor1544.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 임의의 데이터입니다.
                List<String> listTitle = Arrays.asList("No. 0001", "No. 0002", "No. 0003", "No. 0004");
                List<String> listContent = Arrays.asList(
                        "이 자리 좋아요.",
                        "잠잘수 있는 최고의 자리.",
                        "시몬스 침대.",
                        "이 자리에 앉고 인생이 폈습니다."
                );

                binding.floor1544.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveBeforeDialog dialog = new ReserveBeforeDialog(getContext(), "15층 4-4", addCommentData(listTitle, listContent));
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1544.setBackgroundResource(R.drawable.reserve_seat_red);
                    }
                });
            }
        });

        binding.floor1545.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 임의의 데이터입니다.
                List<String> listTitle = Arrays.asList("No. 0001", "No. 0002", "No. 0003", "No. 0004");
                List<String> listContent = Arrays.asList(
                        "이 자리 좋아요.",
                        "잠잘수 있는 최고의 자리.",
                        "시몬스 침대.",
                        "이 자리에 앉고 인생이 폈습니다."
                );

                binding.floor1545.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveBeforeDialog dialog = new ReserveBeforeDialog(getContext(), "15층 4-5", addCommentData(listTitle, listContent));
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1545.setBackgroundResource(R.drawable.reserve_seat_red);
                    }
                });
            }
        });

        binding.floor1546.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 임의의 데이터입니다.
                List<String> listTitle = Arrays.asList("No. 0001", "No. 0002", "No. 0003", "No. 0004");
                List<String> listContent = Arrays.asList(
                        "이 자리 좋아요.",
                        "잠잘수 있는 최고의 자리.",
                        "시몬스 침대.",
                        "이 자리에 앉고 인생이 폈습니다."
                );

                binding.floor1546.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveBeforeDialog dialog = new ReserveBeforeDialog(getContext(), "15층 4-6", addCommentData(listTitle, listContent));
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1546.setBackgroundResource(R.drawable.reserve_seat_red);
                    }
                });
            }
        });

        binding.floor1547.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 임의의 데이터입니다.
                List<String> listTitle = Arrays.asList("No. 0001", "No. 0002", "No. 0003", "No. 0004");
                List<String> listContent = Arrays.asList(
                        "이 자리 좋아요.",
                        "잠잘수 있는 최고의 자리.",
                        "시몬스 침대.",
                        "이 자리에 앉고 인생이 폈습니다."
                );

                binding.floor1547.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveBeforeDialog dialog = new ReserveBeforeDialog(getContext(), "15층 4-7", addCommentData(listTitle, listContent));
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1547.setBackgroundResource(R.drawable.reserve_seat_red);
                    }
                });
            }
        });

        binding.floor1548.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 임의의 데이터입니다.
                List<String> listTitle = Arrays.asList("No. 0001", "No. 0002", "No. 0003", "No. 0004");
                List<String> listContent = Arrays.asList(
                        "이 자리 좋아요.",
                        "잠잘수 있는 최고의 자리.",
                        "시몬스 침대.",
                        "이 자리에 앉고 인생이 폈습니다."
                );

                binding.floor1548.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveBeforeDialog dialog = new ReserveBeforeDialog(getContext(), "15층 4-8", addCommentData(listTitle, listContent));
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1548.setBackgroundResource(R.drawable.reserve_seat_red);
                    }
                });
            }
        });

        binding.floor1549.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 임의의 데이터입니다.
                List<String> listTitle = Arrays.asList("No. 0001", "No. 0002", "No. 0003", "No. 0004");
                List<String> listContent = Arrays.asList(
                        "이 자리 좋아요.",
                        "잠잘수 있는 최고의 자리.",
                        "시몬스 침대.",
                        "이 자리에 앉고 인생이 폈습니다."
                );

                binding.floor1549.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveBeforeDialog dialog = new ReserveBeforeDialog(getContext(), "15층 4-9", addCommentData(listTitle, listContent));
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1549.setBackgroundResource(R.drawable.reserve_seat_red);
                    }
                });
            }
        });


    }

    private ArrayList<ReserveCommentData> addCommentData(List<String> listTitle, List<String> listContent) {
        ArrayList<ReserveCommentData> reserveCommentData = new ArrayList<>();

        for (int i = 0; i < listTitle.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            ReserveCommentData data = new ReserveCommentData();
            data.setTitle(listTitle.get(i));
            data.setContent(listContent.get(i));
            reserveCommentData.add(data);
        }

        return reserveCommentData;
    }

    @Override
    public void onBackPressed() {
        goToMain();
    }

    //프래그먼트 뒤로가기
    private void goToMain(){
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentFrame, mainFragment).commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null; //바인딩 객체를 GC(Garbage Collector) 가 없애도록 하기 위해 참조를 끊기.
    }
}