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
name|gerrit
operator|.
name|server
operator|.
name|index
operator|.
name|SchemaUtil
operator|.
name|schema
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
name|SchemaDefinitions
import|;
end_import

begin_class
DECL|class|AccountSchemaDefinitions
specifier|public
class|class
name|AccountSchemaDefinitions
extends|extends
name|SchemaDefinitions
argument_list|<
name|AccountState
argument_list|>
block|{
annotation|@
name|Deprecated
DECL|field|V4
specifier|static
specifier|final
name|Schema
argument_list|<
name|AccountState
argument_list|>
name|V4
init|=
name|schema
argument_list|(
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
name|FULL_NAME
argument_list|,
name|AccountField
operator|.
name|ID
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
argument_list|,
name|AccountField
operator|.
name|WATCHED_PROJECT
argument_list|)
decl_stmt|;
DECL|field|V5
specifier|static
specifier|final
name|Schema
argument_list|<
name|AccountState
argument_list|>
name|V5
init|=
name|schema
argument_list|(
name|V4
argument_list|,
name|AccountField
operator|.
name|PREFERRED_EMAIL
argument_list|)
decl_stmt|;
DECL|field|INSTANCE
specifier|public
specifier|static
specifier|final
name|AccountSchemaDefinitions
name|INSTANCE
init|=
operator|new
name|AccountSchemaDefinitions
argument_list|()
decl_stmt|;
DECL|method|AccountSchemaDefinitions ()
specifier|private
name|AccountSchemaDefinitions
parameter_list|()
block|{
name|super
argument_list|(
literal|"accounts"
argument_list|,
name|AccountState
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

