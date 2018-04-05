begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
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
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|createNiceMock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|replay
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
name|reviewdb
operator|.
name|client
operator|.
name|Branch
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
name|reviewdb
operator|.
name|client
operator|.
name|Project
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
name|git
operator|.
name|ValidationError
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
DECL|class|DestinationListTest
specifier|public
class|class
name|DestinationListTest
extends|extends
name|TestCase
block|{
DECL|field|R_FOO
specifier|public
specifier|static
specifier|final
name|String
name|R_FOO
init|=
literal|"refs/heads/foo"
decl_stmt|;
DECL|field|R_BAR
specifier|public
specifier|static
specifier|final
name|String
name|R_BAR
init|=
literal|"refs/heads/bar"
decl_stmt|;
DECL|field|P_MY
specifier|public
specifier|static
specifier|final
name|String
name|P_MY
init|=
literal|"myproject"
decl_stmt|;
DECL|field|P_SLASH
specifier|public
specifier|static
specifier|final
name|String
name|P_SLASH
init|=
literal|"my/project/with/slashes"
decl_stmt|;
DECL|field|P_COMPLEX
specifier|public
specifier|static
specifier|final
name|String
name|P_COMPLEX
init|=
literal|" a/project/with spaces and \ttabs "
decl_stmt|;
DECL|field|L_FOO
specifier|public
specifier|static
specifier|final
name|String
name|L_FOO
init|=
name|R_FOO
operator|+
literal|"\t"
operator|+
name|P_MY
operator|+
literal|"\n"
decl_stmt|;
DECL|field|L_BAR
specifier|public
specifier|static
specifier|final
name|String
name|L_BAR
init|=
name|R_BAR
operator|+
literal|"\t"
operator|+
name|P_SLASH
operator|+
literal|"\n"
decl_stmt|;
DECL|field|L_FOO_PAD_F
specifier|public
specifier|static
specifier|final
name|String
name|L_FOO_PAD_F
init|=
literal|" "
operator|+
name|R_FOO
operator|+
literal|"\t"
operator|+
name|P_MY
operator|+
literal|"\n"
decl_stmt|;
DECL|field|L_FOO_PAD_E
specifier|public
specifier|static
specifier|final
name|String
name|L_FOO_PAD_E
init|=
name|R_FOO
operator|+
literal|" \t"
operator|+
name|P_MY
operator|+
literal|"\n"
decl_stmt|;
DECL|field|L_COMPLEX
specifier|public
specifier|static
specifier|final
name|String
name|L_COMPLEX
init|=
name|R_FOO
operator|+
literal|"\t"
operator|+
name|P_COMPLEX
operator|+
literal|"\n"
decl_stmt|;
DECL|field|L_BAD
specifier|public
specifier|static
specifier|final
name|String
name|L_BAD
init|=
name|R_FOO
operator|+
literal|"\n"
decl_stmt|;
DECL|field|HEADER
specifier|public
specifier|static
specifier|final
name|String
name|HEADER
init|=
literal|"# Ref\tProject\n"
decl_stmt|;
DECL|field|HEADER_PROPER
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_PROPER
init|=
literal|"# Ref         \tProject\n"
decl_stmt|;
DECL|field|C1
specifier|public
specifier|static
specifier|final
name|String
name|C1
init|=
literal|"# A Simple Comment\n"
decl_stmt|;
DECL|field|C2
specifier|public
specifier|static
specifier|final
name|String
name|C2
init|=
literal|"# Comment with a tab\t and multi # # #\n"
decl_stmt|;
DECL|field|F_SIMPLE
specifier|public
specifier|static
specifier|final
name|String
name|F_SIMPLE
init|=
name|L_FOO
operator|+
name|L_BAR
decl_stmt|;
DECL|field|F_PROPER
specifier|public
specifier|static
specifier|final
name|String
name|F_PROPER
init|=
name|L_BAR
operator|+
name|L_FOO
decl_stmt|;
comment|// alpha order
DECL|field|F_PAD_F
specifier|public
specifier|static
specifier|final
name|String
name|F_PAD_F
init|=
name|L_FOO_PAD_F
operator|+
name|L_BAR
decl_stmt|;
DECL|field|F_PAD_E
specifier|public
specifier|static
specifier|final
name|String
name|F_PAD_E
init|=
name|L_FOO_PAD_E
operator|+
name|L_BAR
decl_stmt|;
DECL|field|LABEL
specifier|public
specifier|static
specifier|final
name|String
name|LABEL
init|=
literal|"label"
decl_stmt|;
DECL|field|LABEL2
specifier|public
specifier|static
specifier|final
name|String
name|LABEL2
init|=
literal|"another"
decl_stmt|;
DECL|field|B_FOO
specifier|public
specifier|static
specifier|final
name|Branch
operator|.
name|NameKey
name|B_FOO
init|=
name|dest
argument_list|(
name|P_MY
argument_list|,
name|R_FOO
argument_list|)
decl_stmt|;
DECL|field|B_BAR
specifier|public
specifier|static
specifier|final
name|Branch
operator|.
name|NameKey
name|B_BAR
init|=
name|dest
argument_list|(
name|P_SLASH
argument_list|,
name|R_BAR
argument_list|)
decl_stmt|;
DECL|field|B_COMPLEX
specifier|public
specifier|static
specifier|final
name|Branch
operator|.
name|NameKey
name|B_COMPLEX
init|=
name|dest
argument_list|(
name|P_COMPLEX
argument_list|,
name|R_FOO
argument_list|)
decl_stmt|;
DECL|field|D_SIMPLE
specifier|public
specifier|static
specifier|final
name|Set
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|>
name|D_SIMPLE
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
static|static
block|{
name|D_SIMPLE
operator|.
name|clear
argument_list|()
expr_stmt|;
name|D_SIMPLE
operator|.
name|add
argument_list|(
name|B_FOO
argument_list|)
expr_stmt|;
name|D_SIMPLE
operator|.
name|add
argument_list|(
name|B_BAR
argument_list|)
expr_stmt|;
block|}
DECL|method|dest (String project, String ref)
specifier|private
specifier|static
name|Branch
operator|.
name|NameKey
name|dest
parameter_list|(
name|String
name|project
parameter_list|,
name|String
name|ref
parameter_list|)
block|{
return|return
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|project
argument_list|)
argument_list|,
name|ref
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testParseSimple ()
specifier|public
name|void
name|testParseSimple
parameter_list|()
throws|throws
name|Exception
block|{
name|DestinationList
name|dl
init|=
operator|new
name|DestinationList
argument_list|()
decl_stmt|;
name|dl
operator|.
name|parseLabel
argument_list|(
name|LABEL
argument_list|,
name|F_SIMPLE
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|>
name|branches
init|=
name|dl
operator|.
name|getDestinations
argument_list|(
name|LABEL
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|branches
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|D_SIMPLE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testParseWHeader ()
specifier|public
name|void
name|testParseWHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|DestinationList
name|dl
init|=
operator|new
name|DestinationList
argument_list|()
decl_stmt|;
name|dl
operator|.
name|parseLabel
argument_list|(
name|LABEL
argument_list|,
name|HEADER
operator|+
name|F_SIMPLE
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|>
name|branches
init|=
name|dl
operator|.
name|getDestinations
argument_list|(
name|LABEL
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|branches
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|D_SIMPLE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testParseWComments ()
specifier|public
name|void
name|testParseWComments
parameter_list|()
throws|throws
name|Exception
block|{
name|DestinationList
name|dl
init|=
operator|new
name|DestinationList
argument_list|()
decl_stmt|;
name|dl
operator|.
name|parseLabel
argument_list|(
name|LABEL
argument_list|,
name|C1
operator|+
name|F_SIMPLE
operator|+
name|C2
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|>
name|branches
init|=
name|dl
operator|.
name|getDestinations
argument_list|(
name|LABEL
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|branches
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|D_SIMPLE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testParseFooComment ()
specifier|public
name|void
name|testParseFooComment
parameter_list|()
throws|throws
name|Exception
block|{
name|DestinationList
name|dl
init|=
operator|new
name|DestinationList
argument_list|()
decl_stmt|;
name|dl
operator|.
name|parseLabel
argument_list|(
name|LABEL
argument_list|,
literal|"#"
operator|+
name|L_FOO
operator|+
name|L_BAR
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|>
name|branches
init|=
name|dl
operator|.
name|getDestinations
argument_list|(
name|LABEL
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|branches
argument_list|)
operator|.
name|doesNotContain
argument_list|(
name|B_FOO
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|branches
argument_list|)
operator|.
name|contains
argument_list|(
name|B_BAR
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testParsePaddedFronts ()
specifier|public
name|void
name|testParsePaddedFronts
parameter_list|()
throws|throws
name|Exception
block|{
name|DestinationList
name|dl
init|=
operator|new
name|DestinationList
argument_list|()
decl_stmt|;
name|dl
operator|.
name|parseLabel
argument_list|(
name|LABEL
argument_list|,
name|F_PAD_F
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|>
name|branches
init|=
name|dl
operator|.
name|getDestinations
argument_list|(
name|LABEL
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|branches
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|D_SIMPLE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testParsePaddedEnds ()
specifier|public
name|void
name|testParsePaddedEnds
parameter_list|()
throws|throws
name|Exception
block|{
name|DestinationList
name|dl
init|=
operator|new
name|DestinationList
argument_list|()
decl_stmt|;
name|dl
operator|.
name|parseLabel
argument_list|(
name|LABEL
argument_list|,
name|F_PAD_E
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|>
name|branches
init|=
name|dl
operator|.
name|getDestinations
argument_list|(
name|LABEL
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|branches
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|D_SIMPLE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testParseComplex ()
specifier|public
name|void
name|testParseComplex
parameter_list|()
throws|throws
name|Exception
block|{
name|DestinationList
name|dl
init|=
operator|new
name|DestinationList
argument_list|()
decl_stmt|;
name|dl
operator|.
name|parseLabel
argument_list|(
name|LABEL
argument_list|,
name|L_COMPLEX
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|>
name|branches
init|=
name|dl
operator|.
name|getDestinations
argument_list|(
name|LABEL
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|branches
argument_list|)
operator|.
name|contains
argument_list|(
name|B_COMPLEX
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IOException
operator|.
name|class
argument_list|)
DECL|method|testParseBad ()
specifier|public
name|void
name|testParseBad
parameter_list|()
throws|throws
name|IOException
block|{
name|ValidationError
operator|.
name|Sink
name|sink
init|=
name|createNiceMock
argument_list|(
name|ValidationError
operator|.
name|Sink
operator|.
name|class
argument_list|)
decl_stmt|;
name|replay
argument_list|(
name|sink
argument_list|)
expr_stmt|;
operator|new
name|DestinationList
argument_list|()
operator|.
name|parseLabel
argument_list|(
name|LABEL
argument_list|,
name|L_BAD
argument_list|,
name|sink
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testParse2Labels ()
specifier|public
name|void
name|testParse2Labels
parameter_list|()
throws|throws
name|Exception
block|{
name|DestinationList
name|dl
init|=
operator|new
name|DestinationList
argument_list|()
decl_stmt|;
name|dl
operator|.
name|parseLabel
argument_list|(
name|LABEL
argument_list|,
name|F_SIMPLE
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|>
name|branches
init|=
name|dl
operator|.
name|getDestinations
argument_list|(
name|LABEL
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|branches
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|D_SIMPLE
argument_list|)
expr_stmt|;
name|dl
operator|.
name|parseLabel
argument_list|(
name|LABEL2
argument_list|,
name|L_COMPLEX
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|branches
operator|=
name|dl
operator|.
name|getDestinations
argument_list|(
name|LABEL
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|branches
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|D_SIMPLE
argument_list|)
expr_stmt|;
name|branches
operator|=
name|dl
operator|.
name|getDestinations
argument_list|(
name|LABEL2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|branches
argument_list|)
operator|.
name|contains
argument_list|(
name|B_COMPLEX
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAsText ()
specifier|public
name|void
name|testAsText
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|text
init|=
name|HEADER_PROPER
operator|+
literal|"#\n"
operator|+
name|F_PROPER
decl_stmt|;
name|DestinationList
name|dl
init|=
operator|new
name|DestinationList
argument_list|()
decl_stmt|;
name|dl
operator|.
name|parseLabel
argument_list|(
name|LABEL
argument_list|,
name|F_SIMPLE
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|String
name|asText
init|=
name|dl
operator|.
name|asText
argument_list|(
name|LABEL
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|text
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|asText
argument_list|)
expr_stmt|;
name|dl
operator|.
name|parseLabel
argument_list|(
name|LABEL2
argument_list|,
name|asText
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|text
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|dl
operator|.
name|asText
argument_list|(
name|LABEL2
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
