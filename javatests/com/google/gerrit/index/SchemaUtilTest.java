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
DECL|package|com.google.gerrit.index
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|index
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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|index
operator|.
name|SchemaUtil
operator|.
name|getNameParts
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|index
operator|.
name|SchemaUtil
operator|.
name|getPersonParts
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|index
operator|.
name|SchemaUtil
operator|.
name|schema
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|testing
operator|.
name|GerritJUnit
operator|.
name|assertThrows
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|lib
operator|.
name|PersonIdent
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
DECL|class|SchemaUtilTest
specifier|public
class|class
name|SchemaUtilTest
block|{
DECL|class|TestSchemas
specifier|static
class|class
name|TestSchemas
block|{
DECL|field|V1
specifier|static
specifier|final
name|Schema
argument_list|<
name|String
argument_list|>
name|V1
init|=
name|schema
argument_list|()
decl_stmt|;
DECL|field|V2
specifier|static
specifier|final
name|Schema
argument_list|<
name|String
argument_list|>
name|V2
init|=
name|schema
argument_list|()
decl_stmt|;
DECL|field|V3
specifier|static
name|Schema
argument_list|<
name|String
argument_list|>
name|V3
init|=
name|schema
argument_list|()
decl_stmt|;
comment|// Not final, ignored.
DECL|field|V4
specifier|private
specifier|static
specifier|final
name|Schema
argument_list|<
name|String
argument_list|>
name|V4
init|=
name|schema
argument_list|()
decl_stmt|;
comment|// Ignored.
DECL|field|V10
specifier|static
name|Schema
argument_list|<
name|String
argument_list|>
name|V10
init|=
name|schema
argument_list|()
decl_stmt|;
DECL|field|V11
specifier|final
name|Schema
argument_list|<
name|String
argument_list|>
name|V11
init|=
name|schema
argument_list|()
decl_stmt|;
block|}
annotation|@
name|Test
DECL|method|schemasFromClassBuildsMap ()
specifier|public
name|void
name|schemasFromClassBuildsMap
parameter_list|()
block|{
name|Map
argument_list|<
name|Integer
argument_list|,
name|Schema
argument_list|<
name|String
argument_list|>
argument_list|>
name|all
init|=
name|SchemaUtil
operator|.
name|schemasFromClass
argument_list|(
name|TestSchemas
operator|.
name|class
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|all
operator|.
name|keySet
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|all
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|TestSchemas
operator|.
name|V1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|all
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|TestSchemas
operator|.
name|V2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|all
operator|.
name|get
argument_list|(
literal|4
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|TestSchemas
operator|.
name|V4
argument_list|)
expr_stmt|;
name|assertThrows
argument_list|(
name|IllegalArgumentException
operator|.
name|class
argument_list|,
parameter_list|()
lambda|->
name|SchemaUtil
operator|.
name|schemasFromClass
argument_list|(
name|TestSchemas
operator|.
name|class
argument_list|,
name|Object
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getPersonPartsExtractsParts ()
specifier|public
name|void
name|getPersonPartsExtractsParts
parameter_list|()
block|{
comment|// PersonIdent allows empty email, which should be extracted as the empty
comment|// string. However, it converts empty names to null internally.
name|assertThat
argument_list|(
name|getPersonParts
argument_list|(
operator|new
name|PersonIdent
argument_list|(
literal|""
argument_list|,
literal|""
argument_list|)
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getPersonParts
argument_list|(
operator|new
name|PersonIdent
argument_list|(
literal|"foo bar"
argument_list|,
literal|""
argument_list|)
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getPersonParts
argument_list|(
operator|new
name|PersonIdent
argument_list|(
literal|""
argument_list|,
literal|"foo@example.com"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"foo@example.com"
argument_list|,
literal|"foo"
argument_list|,
literal|"example.com"
argument_list|,
literal|"example"
argument_list|,
literal|"com"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getPersonParts
argument_list|(
operator|new
name|PersonIdent
argument_list|(
literal|"foO J. bAr"
argument_list|,
literal|"bA-z@exAmple.cOm"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"foo"
argument_list|,
literal|"j"
argument_list|,
literal|"bar"
argument_list|,
literal|"ba-z@example.com"
argument_list|,
literal|"ba-z"
argument_list|,
literal|"ba"
argument_list|,
literal|"z"
argument_list|,
literal|"example.com"
argument_list|,
literal|"example"
argument_list|,
literal|"com"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getNamePartsExtractsParts ()
specifier|public
name|void
name|getNamePartsExtractsParts
parameter_list|()
block|{
name|assertThat
argument_list|(
name|getNameParts
argument_list|(
literal|""
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|getNameParts
argument_list|(
literal|"foO-bAr_Baz a.b@c/d"
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|,
literal|"baz"
argument_list|,
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"c"
argument_list|,
literal|"d"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

