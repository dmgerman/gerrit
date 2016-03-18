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
DECL|package|com.google.gerrit.server.index.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|index
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
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
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
name|common
operator|.
name|collect
operator|.
name|ImmutableMap
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
name|Iterables
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
name|account
operator|.
name|AccountState
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
name|index
operator|.
name|FieldDef
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
name|index
operator|.
name|Schema
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
name|index
operator|.
name|SchemaUtil
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_class
DECL|class|AccountSchemas
specifier|public
class|class
name|AccountSchemas
block|{
DECL|field|V1
specifier|static
specifier|final
name|Schema
argument_list|<
name|AccountState
argument_list|>
name|V1
init|=
name|schema
argument_list|(
name|AccountField
operator|.
name|ID
argument_list|,
name|AccountField
operator|.
name|ACTIVE
argument_list|,
name|AccountField
operator|.
name|EMAIL
argument_list|,
name|AccountField
operator|.
name|EXTERNAL_ID
argument_list|,
name|AccountField
operator|.
name|NAME_PART
argument_list|,
name|AccountField
operator|.
name|REGISTERED
argument_list|,
name|AccountField
operator|.
name|USERNAME
argument_list|)
decl_stmt|;
DECL|method|schema ( Collection<FieldDef<AccountState, ?>> fields)
specifier|private
specifier|static
name|Schema
argument_list|<
name|AccountState
argument_list|>
name|schema
parameter_list|(
name|Collection
argument_list|<
name|FieldDef
argument_list|<
name|AccountState
argument_list|,
name|?
argument_list|>
argument_list|>
name|fields
parameter_list|)
block|{
return|return
operator|new
name|Schema
argument_list|<>
argument_list|(
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|fields
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|SafeVarargs
DECL|method|schema ( FieldDef<AccountState, ?>.... fields)
specifier|private
specifier|static
name|Schema
argument_list|<
name|AccountState
argument_list|>
name|schema
parameter_list|(
name|FieldDef
argument_list|<
name|AccountState
argument_list|,
name|?
argument_list|>
modifier|...
name|fields
parameter_list|)
block|{
return|return
name|schema
argument_list|(
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|fields
argument_list|)
argument_list|)
return|;
block|}
DECL|field|ALL
specifier|public
specifier|static
specifier|final
name|ImmutableMap
argument_list|<
name|Integer
argument_list|,
name|Schema
argument_list|<
name|AccountState
argument_list|>
argument_list|>
name|ALL
init|=
name|SchemaUtil
operator|.
name|schemasFromClass
argument_list|(
name|AccountSchemas
operator|.
name|class
argument_list|,
name|AccountState
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|get (int version)
specifier|public
specifier|static
name|Schema
argument_list|<
name|AccountState
argument_list|>
name|get
parameter_list|(
name|int
name|version
parameter_list|)
block|{
name|Schema
argument_list|<
name|AccountState
argument_list|>
name|schema
init|=
name|ALL
operator|.
name|get
argument_list|(
name|version
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
name|schema
operator|!=
literal|null
argument_list|,
literal|"Unrecognized schema version: %s"
argument_list|,
name|version
argument_list|)
expr_stmt|;
return|return
name|schema
return|;
block|}
DECL|method|getLatest ()
specifier|public
specifier|static
name|Schema
argument_list|<
name|AccountState
argument_list|>
name|getLatest
parameter_list|()
block|{
return|return
name|Iterables
operator|.
name|getLast
argument_list|(
name|ALL
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

