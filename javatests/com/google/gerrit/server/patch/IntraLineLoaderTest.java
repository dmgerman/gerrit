begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.server.patch
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|patch
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
operator|.
name|UTF_8
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableList
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|diff
operator|.
name|IntraLineDiff
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|diff
operator|.
name|Text
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|diff
operator|.
name|Edit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|diff
operator|.
name|EditList
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|diff
operator|.
name|ReplaceEdit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|IntraLineLoaderTest
specifier|public
class|class
name|IntraLineLoaderTest
block|{
annotation|@
name|Test
DECL|method|rewriteAtStartOfLineIsRecognized ()
specifier|public
name|void
name|rewriteAtStartOfLineIsRecognized
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|a
init|=
literal|"abc1\n"
decl_stmt|;
name|String
name|b
init|=
literal|"def1\n"
decl_stmt|;
name|assertThat
argument_list|(
name|intraline
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ref
argument_list|()
operator|.
name|replace
argument_list|(
literal|"abc"
argument_list|,
literal|"def"
argument_list|)
operator|.
name|common
argument_list|(
literal|"1\n"
argument_list|)
operator|.
name|edits
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|rewriteAtEndOfLineIsRecognized ()
specifier|public
name|void
name|rewriteAtEndOfLineIsRecognized
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|a
init|=
literal|"abc1\n"
decl_stmt|;
name|String
name|b
init|=
literal|"abc2\n"
decl_stmt|;
name|assertThat
argument_list|(
name|intraline
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ref
argument_list|()
operator|.
name|common
argument_list|(
literal|"abc"
argument_list|)
operator|.
name|replace
argument_list|(
literal|"1"
argument_list|,
literal|"2"
argument_list|)
operator|.
name|common
argument_list|(
literal|"\n"
argument_list|)
operator|.
name|edits
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|completeRewriteIncludesNewline ()
specifier|public
name|void
name|completeRewriteIncludesNewline
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|a
init|=
literal|"abc1\n"
decl_stmt|;
name|String
name|b
init|=
literal|"def2\n"
decl_stmt|;
name|assertThat
argument_list|(
name|intraline
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ref
argument_list|()
operator|.
name|replace
argument_list|(
literal|"abc1\n"
argument_list|,
literal|"def2\n"
argument_list|)
operator|.
name|edits
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|closeEditsAreCombined ()
specifier|public
name|void
name|closeEditsAreCombined
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|a
init|=
literal|"ab1cdef2gh\n"
decl_stmt|;
name|String
name|b
init|=
literal|"ab2cdef3gh\n"
decl_stmt|;
name|assertThat
argument_list|(
name|intraline
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ref
argument_list|()
operator|.
name|common
argument_list|(
literal|"ab"
argument_list|)
operator|.
name|replace
argument_list|(
literal|"1cdef2"
argument_list|,
literal|"2cdef3"
argument_list|)
operator|.
name|common
argument_list|(
literal|"gh\n"
argument_list|)
operator|.
name|edits
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|preferInsertAfterCommonPart1 ()
specifier|public
name|void
name|preferInsertAfterCommonPart1
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|a
init|=
literal|"start middle end\n"
decl_stmt|;
name|String
name|b
init|=
literal|"start middlemiddle end\n"
decl_stmt|;
name|assertThat
argument_list|(
name|intraline
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ref
argument_list|()
operator|.
name|common
argument_list|(
literal|"start middle"
argument_list|)
operator|.
name|insert
argument_list|(
literal|"middle"
argument_list|)
operator|.
name|common
argument_list|(
literal|" end\n"
argument_list|)
operator|.
name|edits
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|preferInsertAfterCommonPart2 ()
specifier|public
name|void
name|preferInsertAfterCommonPart2
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|a
init|=
literal|"abc def\n"
decl_stmt|;
name|String
name|b
init|=
literal|"abc  def\n"
decl_stmt|;
name|assertThat
argument_list|(
name|intraline
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ref
argument_list|()
operator|.
name|common
argument_list|(
literal|"abc "
argument_list|)
operator|.
name|insert
argument_list|(
literal|" "
argument_list|)
operator|.
name|common
argument_list|(
literal|"def\n"
argument_list|)
operator|.
name|edits
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|preferInsertAtLineBreak1 ()
specifier|public
name|void
name|preferInsertAtLineBreak1
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|a
init|=
literal|"multi\nline\n"
decl_stmt|;
name|String
name|b
init|=
literal|"multi\nlinemulti\nline\n"
decl_stmt|;
name|assertThat
argument_list|(
name|intraline
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|wordEdit
argument_list|(
literal|10
argument_list|,
literal|10
argument_list|,
literal|6
argument_list|,
literal|16
argument_list|)
argument_list|)
expr_stmt|;
comment|// better would be:
comment|//assertThat(intraline(a, b)).isEqualTo(wordEdit(6, 6, 6, 16));
comment|// or the equivalent:
comment|//assertThat(intraline(a, b)).isEqualTo(ref()
comment|//    .common("multi\n").insert("linemulti\n").common("line\n").edits
comment|//);
block|}
comment|//TODO: expected failure
comment|// the current code does not work on the first line
comment|// and the insert marker is in the wrong location
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|AssertionError
operator|.
name|class
argument_list|)
DECL|method|preferInsertAtLineBreak2 ()
specifier|public
name|void
name|preferInsertAtLineBreak2
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|a
init|=
literal|"  abc\n    def\n"
decl_stmt|;
name|String
name|b
init|=
literal|"    abc\n      def\n"
decl_stmt|;
name|assertThat
argument_list|(
name|intraline
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ref
argument_list|()
operator|.
name|insert
argument_list|(
literal|"  "
argument_list|)
operator|.
name|common
argument_list|(
literal|"  abc\n"
argument_list|)
operator|.
name|insert
argument_list|(
literal|"  "
argument_list|)
operator|.
name|common
argument_list|(
literal|"  def\n"
argument_list|)
operator|.
name|edits
argument_list|)
expr_stmt|;
block|}
comment|//TODO: expected failure
comment|// the current code does not work on the first line
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|AssertionError
operator|.
name|class
argument_list|)
DECL|method|preferDeleteAtLineBreak ()
specifier|public
name|void
name|preferDeleteAtLineBreak
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|a
init|=
literal|"    abc\n      def\n"
decl_stmt|;
name|String
name|b
init|=
literal|"  abc\n    def\n"
decl_stmt|;
name|assertThat
argument_list|(
name|intraline
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ref
argument_list|()
operator|.
name|remove
argument_list|(
literal|"  "
argument_list|)
operator|.
name|common
argument_list|(
literal|"  abc\n"
argument_list|)
operator|.
name|remove
argument_list|(
literal|"  "
argument_list|)
operator|.
name|common
argument_list|(
literal|"  def\n"
argument_list|)
operator|.
name|edits
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|insertedWhitespaceIsRecognized ()
specifier|public
name|void
name|insertedWhitespaceIsRecognized
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|a
init|=
literal|" int *foobar\n"
decl_stmt|;
name|String
name|b
init|=
literal|" int * foobar\n"
decl_stmt|;
name|assertThat
argument_list|(
name|intraline
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ref
argument_list|()
operator|.
name|common
argument_list|(
literal|" int *"
argument_list|)
operator|.
name|insert
argument_list|(
literal|" "
argument_list|)
operator|.
name|common
argument_list|(
literal|"foobar\n"
argument_list|)
operator|.
name|edits
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|insertedWhitespaceIsRecognizedInMultipleLines ()
specifier|public
name|void
name|insertedWhitespaceIsRecognizedInMultipleLines
parameter_list|()
throws|throws
name|Exception
block|{
comment|//         |0    5   10  |  5   20    5   30
name|String
name|a
init|=
literal|" int *foobar\n int *foobar\n"
decl_stmt|;
name|String
name|b
init|=
literal|" int * foobar\n int * foobar\n"
decl_stmt|;
name|assertThat
argument_list|(
name|intraline
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ref
argument_list|()
operator|.
name|common
argument_list|(
literal|" int *"
argument_list|)
operator|.
name|insert
argument_list|(
literal|" "
argument_list|)
operator|.
name|common
argument_list|(
literal|"foobar\n"
argument_list|)
operator|.
name|common
argument_list|(
literal|" int *"
argument_list|)
operator|.
name|insert
argument_list|(
literal|" "
argument_list|)
operator|.
name|common
argument_list|(
literal|"foobar\n"
argument_list|)
operator|.
name|edits
argument_list|)
expr_stmt|;
block|}
comment|// helper functions to call IntraLineLoader.compute
DECL|method|countLines (String s)
specifier|private
specifier|static
name|int
name|countLines
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|int
name|count
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|s
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|s
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
operator|==
literal|'\n'
condition|)
block|{
name|count
operator|++
expr_stmt|;
block|}
block|}
return|return
name|count
return|;
block|}
DECL|method|intraline (String a, String b)
specifier|private
specifier|static
name|List
argument_list|<
name|Edit
argument_list|>
name|intraline
parameter_list|(
name|String
name|a
parameter_list|,
name|String
name|b
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|intraline
argument_list|(
name|a
argument_list|,
name|b
argument_list|,
operator|new
name|Edit
argument_list|(
literal|0
argument_list|,
name|countLines
argument_list|(
name|a
argument_list|)
argument_list|,
literal|0
argument_list|,
name|countLines
argument_list|(
name|b
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
DECL|method|intraline (String a, String b, Edit lines)
specifier|private
specifier|static
name|List
argument_list|<
name|Edit
argument_list|>
name|intraline
parameter_list|(
name|String
name|a
parameter_list|,
name|String
name|b
parameter_list|,
name|Edit
name|lines
parameter_list|)
throws|throws
name|Exception
block|{
name|Text
name|aText
init|=
operator|new
name|Text
argument_list|(
name|a
operator|.
name|getBytes
argument_list|(
name|UTF_8
argument_list|)
argument_list|)
decl_stmt|;
name|Text
name|bText
init|=
operator|new
name|Text
argument_list|(
name|b
operator|.
name|getBytes
argument_list|(
name|UTF_8
argument_list|)
argument_list|)
decl_stmt|;
name|IntraLineDiff
name|diff
init|=
name|IntraLineLoader
operator|.
name|compute
argument_list|(
name|aText
argument_list|,
name|bText
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
name|lines
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|diff
operator|.
name|getStatus
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|IntraLineDiff
operator|.
name|Status
operator|.
name|EDIT_LIST
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Edit
argument_list|>
name|actualEdits
init|=
name|diff
operator|.
name|getEdits
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|actualEdits
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Edit
name|actualEdit
init|=
name|actualEdits
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|actualEdit
operator|.
name|getBeginA
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|lines
operator|.
name|getBeginA
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|actualEdit
operator|.
name|getEndA
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|lines
operator|.
name|getEndA
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|actualEdit
operator|.
name|getBeginB
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|lines
operator|.
name|getBeginB
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|actualEdit
operator|.
name|getEndB
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|lines
operator|.
name|getEndB
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|actualEdit
argument_list|)
operator|.
name|isInstanceOf
argument_list|(
name|ReplaceEdit
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
operator|(
operator|(
name|ReplaceEdit
operator|)
name|actualEdit
operator|)
operator|.
name|getInternalEdits
argument_list|()
return|;
block|}
comment|// helpers to compute reference values
DECL|method|wordEdit (int as, int ae, int bs, int be)
specifier|private
specifier|static
name|List
argument_list|<
name|Edit
argument_list|>
name|wordEdit
parameter_list|(
name|int
name|as
parameter_list|,
name|int
name|ae
parameter_list|,
name|int
name|bs
parameter_list|,
name|int
name|be
parameter_list|)
block|{
return|return
name|EditList
operator|.
name|singleton
argument_list|(
operator|new
name|Edit
argument_list|(
name|as
argument_list|,
name|ae
argument_list|,
name|bs
argument_list|,
name|be
argument_list|)
argument_list|)
return|;
block|}
DECL|method|ref ()
specifier|private
specifier|static
name|Reference
name|ref
parameter_list|()
block|{
return|return
operator|new
name|Reference
argument_list|()
return|;
block|}
DECL|class|Reference
specifier|private
specifier|static
class|class
name|Reference
block|{
DECL|field|edits
name|List
argument_list|<
name|Edit
argument_list|>
name|edits
decl_stmt|;
DECL|field|posA
specifier|private
name|int
name|posA
decl_stmt|;
DECL|field|posB
specifier|private
name|int
name|posB
decl_stmt|;
DECL|method|Reference ()
name|Reference
parameter_list|()
block|{
name|edits
operator|=
operator|new
name|EditList
argument_list|()
expr_stmt|;
name|posA
operator|=
name|posB
operator|=
literal|0
expr_stmt|;
block|}
DECL|method|common (String s)
name|Reference
name|common
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|int
name|len
init|=
name|s
operator|.
name|length
argument_list|()
decl_stmt|;
name|posA
operator|+=
name|len
expr_stmt|;
name|posB
operator|+=
name|len
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|insert (String s)
name|Reference
name|insert
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|replace
argument_list|(
literal|""
argument_list|,
name|s
argument_list|)
return|;
block|}
DECL|method|remove (String s)
name|Reference
name|remove
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|replace
argument_list|(
name|s
argument_list|,
literal|""
argument_list|)
return|;
block|}
DECL|method|replace (String a, String b)
name|Reference
name|replace
parameter_list|(
name|String
name|a
parameter_list|,
name|String
name|b
parameter_list|)
block|{
name|int
name|lenA
init|=
name|a
operator|.
name|length
argument_list|()
decl_stmt|;
name|int
name|lenB
init|=
name|b
operator|.
name|length
argument_list|()
decl_stmt|;
name|edits
operator|.
name|add
argument_list|(
operator|new
name|Edit
argument_list|(
name|posA
argument_list|,
name|posA
operator|+
name|lenA
argument_list|,
name|posB
argument_list|,
name|posB
operator|+
name|lenB
argument_list|)
argument_list|)
expr_stmt|;
name|posA
operator|+=
name|lenA
expr_stmt|;
name|posB
operator|+=
name|lenB
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
block|}
end_class

end_unit

