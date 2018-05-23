package com.company;

import java.util.ArrayList;
import java.util.HashMap;


public class Pattern {
    private static final int Blank = 294;
    private final State start;
    private boolean isPrefix;
    private boolean isSuffix;
    private final DState dStart;
    private HashMap<NfaList, DState> allDStates = new HashMap<NfaList,DState>();

    private static final State matchState = new State(257);
    private static final DState deadState = new DState();
    private static final State blank = new State(Blank);
    private static int listid = 0;
    private ArrayList<State> l1 = new ArrayList<State>();
    private ArrayList<State> l2 = new ArrayList<State>();

    public static Pattern compile(String pattern)
    {
        return new Pattern(pattern);
    }

    private Pattern(String pattern)
    {
        Parser parser = new Parser(pattern);
        RegEx re = parser.parse();
        isPrefix = re.getIsPrefix();
        isSuffix = re.getIsSuffix();
        Frag f = Regex2NFA(re);
        patch(f.out, matchState);
        patch1(f.out1, matchState);
        this.start = f.start;
        NfaList startNFA = new NfaList(startList(start, l1));
        dStart = new DState(startNFA);
        allDStates.put(startNFA, dStart);
    }

    private Frag Regex2NFA(RegEx re)
    {
        switch (re.type) {
            case Sequence:
            {
                Sequence sequence = (Sequence) re;
                if (sequence.first == RegEx.blank)
                {
                    Frag f = Regex2NFA(sequence.second);
                    return f;
                }
                else
                {
                    Frag f1 = Regex2NFA(sequence.first);
                    Frag f2 = Regex2NFA(sequence.second);
                    patch(f1.out, f2.start);
                    patch1(f1.out1, f2.start);
                    return new Frag(f1.start, f2.out, f2.out1);
                }
            }
            case Choice:
            {
                Choice choice = (Choice) re;
                Frag f1 = Regex2NFA(choice.thisOne);
                Frag f2 = Regex2NFA(choice.thatOne);
                State s = new State(Split, f1.start, f2.start);
                return new Frag(s, append(f1.out, f2.out), append(f1.out1,
                        f2.out1));
            }
            case PlusRepetition:
            {
                RegEx in = ((PlusRepetition) re).internal;
                Frag f = Regex2NFA(in);
                State s = new State(Split, f.start, null);
                patch(f.out, s);
                patch1(f.out1, s);
                return new Frag(f.start, new ArrayList<State>(), list1(s));
            }
            case StarRepetition:
            {
                RegEx in = ((StarRepetition) re).internal;
                Frag f = Regex2NFA(in);
                State s = new State(Split, f.start, null);
                patch(f.out, s);
                patch1(f.out1, s);
                return new Frag(s, new ArrayList<State>(), list1(s));
            }
            case QuoraRepetition:
            {
                RegEx in = ((QuoraRepetition) re).internal;
                Frag f = Regex2NFA(in);
                State s = new State(Split, f.start, null);
                return new Frag(s, f.out, append(f.out1, list1(s)));
            }
            case Primitive:
            {
                char c = ((Primitives) re).c;
                State s = new State(c);
                return new Frag(s, list1(s));
            }
            case AnyChar:
            {
                State s = new State(AnyChar);
                return new Frag(s, list1(s));
            }
            case SpecialChars:
            {
                char c = ((SpecialChars) re).c;
                State s;
                switch (c)
                {
                    case 's':
                        s = new State(SingleLetter);
                        break;
                    case 'S':
                        s = new State(NonSingleLetter);
                        break;
                    case 'w':
                        s = new State(Word);
                        break;
                    case 'W':
                        s = new State(NonWord);
                        break;
                    default:
                        s = new State(UnSupported);
                        break;
                }
                return new Frag(s, list1(s));
            }
            case Blank:
                return new Frag(blank, list1(blank));
            case MultiChoice:
            {
                MultiChoice mc = (MultiChoice) re;
                ScaleState ss = new ScaleState(mc.scale);
                return new Frag(ss, list1(ss));
            }
            default:
                throw new RuntimeException(re.toString());
        }
    }

    private void patch(ArrayList<State> l, State s)
    {
        for (int i = 0; i < l.size(); i++)
        {
            l.get(i).out = s;
        }

    }

    public void patch1(ArrayList<State> l, State s)
    {
        for (int i = 0; i < l.size(); i++)
        {
            l.get(i).out1 = s;
        }
    }

    /* Helper methods for generating nfa */
    private ArrayList<State> list1(State outp)
    {
        ArrayList<State> list = new ArrayList<State>();
        list.add(outp);
        return list;
    }

    private ArrayList<State> append(ArrayList<State> l1, ArrayList<State> l2)
    {
        l1.addAll(l2);
        return l1;
    }
    /* Methods used to match strings */
    private ArrayList<State> startList(State s, ArrayList<State> l)
    {
        listid++;
        l.clear();
        addState(l, s);
        return l;
    }

    private void addState(ArrayList<State> l, State s)
    {
        if (s == null || s.lastlist1 == listid) return;
        s.lastlist1 = listid;
        if (s.c == Split)
        {
            addState(l, s.out);
            addState(l, s.out1);
            return;
        }
        if (s.c == Blank)
        {
            addState(l, s.out);
            return;
        }
        l.add(s);
    }

    /* Special characters for State transformation */
    private static final int Split = 256;
    private static final int Tab = 280;
    private static final int Newline = 281;
    private static final int Return = 282;
    private static final int Digit = 283;
    private static final int WhiteSpace = 284;
    private static final int NonWhiteSpace = 285;
    private static final int SingleLetter = 286;
    private static final int NonSingleLetter = 287;
    private static final int NewPage = 288;
    private static final int Escape = 289;
    private static final int Boundary = 290;
    private static final int NonBoundary = 291;
    private static final int AnyChar = 292;
    private static final int UnSupported = 293;
    protected static final int Scale = 295;
    protected static final int NegScale = 298;
    private static final int Word = 296;
    private static final int NonWord = 297;
}
