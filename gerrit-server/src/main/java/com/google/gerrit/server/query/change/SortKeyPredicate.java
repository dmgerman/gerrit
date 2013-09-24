begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.query.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|query
operator|.
name|change
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
name|checkNotNull
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
name|server
operator|.
name|index
operator|.
name|ChangeField
operator|.
name|SORTKEY
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
name|common
operator|.
name|Nullable
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
name|Change
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
name|server
operator|.
name|ReviewDb
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
name|ChangeUtil
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
name|ChangeField
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
name|IndexPredicate
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
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
import|;
end_import

begin_class
DECL|class|SortKeyPredicate
specifier|public
specifier|abstract
class|class
name|SortKeyPredicate
extends|extends
name|IndexPredicate
argument_list|<
name|ChangeData
argument_list|>
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
DECL|method|parseSortKey (Schema<ChangeData> schema, String value)
specifier|private
specifier|static
name|long
name|parseSortKey
parameter_list|(
name|Schema
argument_list|<
name|ChangeData
argument_list|>
name|schema
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|FieldDef
argument_list|<
name|ChangeData
argument_list|,
name|?
argument_list|>
name|field
init|=
name|schema
operator|.
name|getFields
argument_list|()
operator|.
name|get
argument_list|(
name|SORTKEY
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|field
operator|==
name|SORTKEY
condition|)
block|{
return|return
name|ChangeUtil
operator|.
name|parseSortKey
argument_list|(
name|value
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|ChangeField
operator|.
name|legacyParseSortKey
argument_list|(
name|value
argument_list|)
return|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
DECL|method|sortkeyField (Schema<ChangeData> schema)
specifier|private
specifier|static
name|FieldDef
argument_list|<
name|ChangeData
argument_list|,
name|?
argument_list|>
name|sortkeyField
parameter_list|(
name|Schema
argument_list|<
name|ChangeData
argument_list|>
name|schema
parameter_list|)
block|{
if|if
condition|(
name|schema
operator|==
literal|null
condition|)
block|{
return|return
name|ChangeField
operator|.
name|LEGACY_SORTKEY
return|;
block|}
name|FieldDef
argument_list|<
name|ChangeData
argument_list|,
name|?
argument_list|>
name|f
init|=
name|schema
operator|.
name|getFields
argument_list|()
operator|.
name|get
argument_list|(
name|SORTKEY
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|!=
literal|null
condition|)
block|{
return|return
name|f
return|;
block|}
return|return
name|checkNotNull
argument_list|(
name|schema
operator|.
name|getFields
argument_list|()
operator|.
name|get
argument_list|(
name|ChangeField
operator|.
name|LEGACY_SORTKEY
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|,
literal|"schema missing sortkey field, found: %s"
argument_list|,
name|schema
operator|.
name|getFields
argument_list|()
operator|.
name|keySet
argument_list|()
argument_list|)
return|;
block|}
DECL|field|schema
specifier|protected
specifier|final
name|Schema
argument_list|<
name|ChangeData
argument_list|>
name|schema
decl_stmt|;
DECL|field|dbProvider
specifier|protected
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
decl_stmt|;
DECL|method|SortKeyPredicate (Schema<ChangeData> schema, Provider<ReviewDb> dbProvider, String name, String value)
name|SortKeyPredicate
parameter_list|(
name|Schema
argument_list|<
name|ChangeData
argument_list|>
name|schema
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|sortkeyField
argument_list|(
name|schema
argument_list|)
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|this
operator|.
name|schema
operator|=
name|schema
expr_stmt|;
name|this
operator|.
name|dbProvider
operator|=
name|dbProvider
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getCost ()
specifier|public
name|int
name|getCost
parameter_list|()
block|{
return|return
literal|1
return|;
block|}
DECL|method|getMinValue (Schema<ChangeData> schema)
specifier|public
specifier|abstract
name|long
name|getMinValue
parameter_list|(
name|Schema
argument_list|<
name|ChangeData
argument_list|>
name|schema
parameter_list|)
function_decl|;
DECL|method|getMaxValue (Schema<ChangeData> schema)
specifier|public
specifier|abstract
name|long
name|getMaxValue
parameter_list|(
name|Schema
argument_list|<
name|ChangeData
argument_list|>
name|schema
parameter_list|)
function_decl|;
DECL|method|copy (String newValue)
specifier|public
specifier|abstract
name|SortKeyPredicate
name|copy
parameter_list|(
name|String
name|newValue
parameter_list|)
function_decl|;
DECL|class|Before
specifier|public
specifier|static
class|class
name|Before
extends|extends
name|SortKeyPredicate
block|{
DECL|method|Before (@ullable Schema<ChangeData> schema, Provider<ReviewDb> dbProvider, String value)
name|Before
parameter_list|(
annotation|@
name|Nullable
name|Schema
argument_list|<
name|ChangeData
argument_list|>
name|schema
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|schema
argument_list|,
name|dbProvider
argument_list|,
literal|"sortkey_before"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getMinValue (Schema<ChangeData> schema)
specifier|public
name|long
name|getMinValue
parameter_list|(
name|Schema
argument_list|<
name|ChangeData
argument_list|>
name|schema
parameter_list|)
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|getMaxValue (Schema<ChangeData> schema)
specifier|public
name|long
name|getMaxValue
parameter_list|(
name|Schema
argument_list|<
name|ChangeData
argument_list|>
name|schema
parameter_list|)
block|{
return|return
name|parseSortKey
argument_list|(
name|schema
argument_list|,
name|getValue
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|match (ChangeData cd)
specifier|public
name|boolean
name|match
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
throws|throws
name|OrmException
block|{
name|Change
name|change
init|=
name|cd
operator|.
name|change
argument_list|(
name|dbProvider
argument_list|)
decl_stmt|;
return|return
name|change
operator|!=
literal|null
operator|&&
name|change
operator|.
name|getSortKey
argument_list|()
operator|.
name|compareTo
argument_list|(
name|getValue
argument_list|()
argument_list|)
operator|<
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|copy (String newValue)
specifier|public
name|Before
name|copy
parameter_list|(
name|String
name|newValue
parameter_list|)
block|{
return|return
operator|new
name|Before
argument_list|(
name|schema
argument_list|,
name|dbProvider
argument_list|,
name|newValue
argument_list|)
return|;
block|}
block|}
DECL|class|After
specifier|public
specifier|static
class|class
name|After
extends|extends
name|SortKeyPredicate
block|{
DECL|method|After (@ullable Schema<ChangeData> schema, Provider<ReviewDb> dbProvider, String value)
name|After
parameter_list|(
annotation|@
name|Nullable
name|Schema
argument_list|<
name|ChangeData
argument_list|>
name|schema
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|schema
argument_list|,
name|dbProvider
argument_list|,
literal|"sortkey_after"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getMinValue (Schema<ChangeData> schema)
specifier|public
name|long
name|getMinValue
parameter_list|(
name|Schema
argument_list|<
name|ChangeData
argument_list|>
name|schema
parameter_list|)
block|{
return|return
name|parseSortKey
argument_list|(
name|schema
argument_list|,
name|getValue
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getMaxValue (Schema<ChangeData> schema)
specifier|public
name|long
name|getMaxValue
parameter_list|(
name|Schema
argument_list|<
name|ChangeData
argument_list|>
name|schema
parameter_list|)
block|{
return|return
name|Long
operator|.
name|MAX_VALUE
return|;
block|}
annotation|@
name|Override
DECL|method|match (ChangeData cd)
specifier|public
name|boolean
name|match
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
throws|throws
name|OrmException
block|{
name|Change
name|change
init|=
name|cd
operator|.
name|change
argument_list|(
name|dbProvider
argument_list|)
decl_stmt|;
return|return
name|change
operator|!=
literal|null
operator|&&
name|change
operator|.
name|getSortKey
argument_list|()
operator|.
name|compareTo
argument_list|(
name|getValue
argument_list|()
argument_list|)
operator|>
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|copy (String newValue)
specifier|public
name|After
name|copy
parameter_list|(
name|String
name|newValue
parameter_list|)
block|{
return|return
operator|new
name|After
argument_list|(
name|schema
argument_list|,
name|dbProvider
argument_list|,
name|newValue
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

