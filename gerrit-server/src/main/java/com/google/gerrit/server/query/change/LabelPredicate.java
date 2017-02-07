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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Lists
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
name|Account
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
name|AccountGroup
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
name|IdentifiedUser
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
name|change
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
name|project
operator|.
name|ChangeControl
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
name|project
operator|.
name|ProjectCache
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
name|query
operator|.
name|OrPredicate
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
name|query
operator|.
name|Predicate
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
name|util
operator|.
name|LabelVote
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
name|util
operator|.
name|RangeUtil
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
name|util
operator|.
name|RangeUtil
operator|.
name|Range
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
DECL|class|LabelPredicate
specifier|public
class|class
name|LabelPredicate
extends|extends
name|OrPredicate
argument_list|<
name|ChangeData
argument_list|>
block|{
DECL|field|MAX_LABEL_VALUE
specifier|private
specifier|static
specifier|final
name|int
name|MAX_LABEL_VALUE
init|=
literal|4
decl_stmt|;
DECL|class|Args
specifier|static
class|class
name|Args
block|{
DECL|field|field
specifier|final
name|FieldDef
argument_list|<
name|ChangeData
argument_list|,
name|?
argument_list|>
name|field
decl_stmt|;
DECL|field|projectCache
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|ccFactory
specifier|final
name|ChangeControl
operator|.
name|GenericFactory
name|ccFactory
decl_stmt|;
DECL|field|userFactory
specifier|final
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
decl_stmt|;
DECL|field|dbProvider
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
decl_stmt|;
DECL|field|value
specifier|final
name|String
name|value
decl_stmt|;
DECL|field|accounts
specifier|final
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|accounts
decl_stmt|;
DECL|field|group
specifier|final
name|AccountGroup
operator|.
name|UUID
name|group
decl_stmt|;
DECL|method|Args ( FieldDef<ChangeData, ?> field, ProjectCache projectCache, ChangeControl.GenericFactory ccFactory, IdentifiedUser.GenericFactory userFactory, Provider<ReviewDb> dbProvider, String value, Set<Account.Id> accounts, AccountGroup.UUID group)
specifier|private
name|Args
parameter_list|(
name|FieldDef
argument_list|<
name|ChangeData
argument_list|,
name|?
argument_list|>
name|field
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|,
name|ChangeControl
operator|.
name|GenericFactory
name|ccFactory
parameter_list|,
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
parameter_list|,
name|String
name|value
parameter_list|,
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|accounts
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|group
parameter_list|)
block|{
name|this
operator|.
name|field
operator|=
name|field
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|ccFactory
operator|=
name|ccFactory
expr_stmt|;
name|this
operator|.
name|userFactory
operator|=
name|userFactory
expr_stmt|;
name|this
operator|.
name|dbProvider
operator|=
name|dbProvider
expr_stmt|;
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
name|this
operator|.
name|accounts
operator|=
name|accounts
expr_stmt|;
name|this
operator|.
name|group
operator|=
name|group
expr_stmt|;
block|}
block|}
DECL|class|Parsed
specifier|private
specifier|static
class|class
name|Parsed
block|{
DECL|field|label
specifier|private
specifier|final
name|String
name|label
decl_stmt|;
DECL|field|test
specifier|private
specifier|final
name|String
name|test
decl_stmt|;
DECL|field|expVal
specifier|private
specifier|final
name|int
name|expVal
decl_stmt|;
DECL|method|Parsed (String label, String test, int expVal)
specifier|private
name|Parsed
parameter_list|(
name|String
name|label
parameter_list|,
name|String
name|test
parameter_list|,
name|int
name|expVal
parameter_list|)
block|{
name|this
operator|.
name|label
operator|=
name|label
expr_stmt|;
name|this
operator|.
name|test
operator|=
name|test
expr_stmt|;
name|this
operator|.
name|expVal
operator|=
name|expVal
expr_stmt|;
block|}
block|}
DECL|field|value
specifier|private
specifier|final
name|String
name|value
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
DECL|method|LabelPredicate ( ChangeQueryBuilder.Arguments a, String value, Set<Account.Id> accounts, AccountGroup.UUID group)
name|LabelPredicate
parameter_list|(
name|ChangeQueryBuilder
operator|.
name|Arguments
name|a
parameter_list|,
name|String
name|value
parameter_list|,
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|accounts
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|group
parameter_list|)
block|{
name|super
argument_list|(
name|predicates
argument_list|(
operator|new
name|Args
argument_list|(
name|a
operator|.
name|getSchema
argument_list|()
operator|.
name|getField
argument_list|(
name|ChangeField
operator|.
name|LABEL2
argument_list|,
name|ChangeField
operator|.
name|LABEL
argument_list|)
operator|.
name|get
argument_list|()
argument_list|,
name|a
operator|.
name|projectCache
argument_list|,
name|a
operator|.
name|changeControlGenericFactory
argument_list|,
name|a
operator|.
name|userFactory
argument_list|,
name|a
operator|.
name|db
argument_list|,
name|value
argument_list|,
name|accounts
argument_list|,
name|group
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
DECL|method|predicates (Args args)
specifier|private
specifier|static
name|List
argument_list|<
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
argument_list|>
name|predicates
parameter_list|(
name|Args
name|args
parameter_list|)
block|{
name|String
name|v
init|=
name|args
operator|.
name|value
decl_stmt|;
name|Parsed
name|parsed
init|=
literal|null
decl_stmt|;
try|try
block|{
name|LabelVote
name|lv
init|=
name|LabelVote
operator|.
name|parse
argument_list|(
name|v
argument_list|)
decl_stmt|;
name|parsed
operator|=
operator|new
name|Parsed
argument_list|(
name|lv
operator|.
name|label
argument_list|()
argument_list|,
literal|"="
argument_list|,
name|lv
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// Try next format.
block|}
try|try
block|{
name|LabelVote
name|lv
init|=
name|LabelVote
operator|.
name|parseWithEquals
argument_list|(
name|v
argument_list|)
decl_stmt|;
name|parsed
operator|=
operator|new
name|Parsed
argument_list|(
name|lv
operator|.
name|label
argument_list|()
argument_list|,
literal|"="
argument_list|,
name|lv
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// Try next format.
block|}
name|Range
name|range
decl_stmt|;
if|if
condition|(
name|parsed
operator|==
literal|null
condition|)
block|{
name|range
operator|=
name|RangeUtil
operator|.
name|getRange
argument_list|(
name|v
argument_list|,
operator|-
name|MAX_LABEL_VALUE
argument_list|,
name|MAX_LABEL_VALUE
argument_list|)
expr_stmt|;
if|if
condition|(
name|range
operator|==
literal|null
condition|)
block|{
name|range
operator|=
operator|new
name|Range
argument_list|(
name|v
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|range
operator|=
name|RangeUtil
operator|.
name|getRange
argument_list|(
name|parsed
operator|.
name|label
argument_list|,
name|parsed
operator|.
name|test
argument_list|,
name|parsed
operator|.
name|expVal
argument_list|,
operator|-
name|MAX_LABEL_VALUE
argument_list|,
name|MAX_LABEL_VALUE
argument_list|)
expr_stmt|;
block|}
name|String
name|prefix
init|=
name|range
operator|.
name|prefix
decl_stmt|;
name|int
name|min
init|=
name|range
operator|.
name|min
decl_stmt|;
name|int
name|max
init|=
name|range
operator|.
name|max
decl_stmt|;
name|List
argument_list|<
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
argument_list|>
name|r
init|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|max
operator|-
name|min
operator|+
literal|1
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|min
init|;
name|i
operator|<=
name|max
condition|;
name|i
operator|++
control|)
block|{
name|r
operator|.
name|add
argument_list|(
name|onePredicate
argument_list|(
name|args
argument_list|,
name|prefix
argument_list|,
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
DECL|method|onePredicate (Args args, String label, int expVal)
specifier|private
specifier|static
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|onePredicate
parameter_list|(
name|Args
name|args
parameter_list|,
name|String
name|label
parameter_list|,
name|int
name|expVal
parameter_list|)
block|{
if|if
condition|(
name|expVal
operator|!=
literal|0
condition|)
block|{
return|return
name|equalsLabelPredicate
argument_list|(
name|args
argument_list|,
name|label
argument_list|,
name|expVal
argument_list|)
return|;
block|}
return|return
name|noLabelQuery
argument_list|(
name|args
argument_list|,
name|label
argument_list|)
return|;
block|}
DECL|method|noLabelQuery (Args args, String label)
specifier|private
specifier|static
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|noLabelQuery
parameter_list|(
name|Args
name|args
parameter_list|,
name|String
name|label
parameter_list|)
block|{
name|List
argument_list|<
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
argument_list|>
name|r
init|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
literal|2
operator|*
name|MAX_LABEL_VALUE
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
name|MAX_LABEL_VALUE
condition|;
name|i
operator|++
control|)
block|{
name|r
operator|.
name|add
argument_list|(
name|equalsLabelPredicate
argument_list|(
name|args
argument_list|,
name|label
argument_list|,
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|add
argument_list|(
name|equalsLabelPredicate
argument_list|(
name|args
argument_list|,
name|label
argument_list|,
operator|-
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|not
argument_list|(
name|or
argument_list|(
name|r
argument_list|)
argument_list|)
return|;
block|}
DECL|method|equalsLabelPredicate (Args args, String label, int expVal)
specifier|private
specifier|static
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|equalsLabelPredicate
parameter_list|(
name|Args
name|args
parameter_list|,
name|String
name|label
parameter_list|,
name|int
name|expVal
parameter_list|)
block|{
if|if
condition|(
name|args
operator|.
name|accounts
operator|==
literal|null
operator|||
name|args
operator|.
name|accounts
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
operator|new
name|EqualsLabelPredicate
argument_list|(
name|args
argument_list|,
name|label
argument_list|,
name|expVal
argument_list|,
literal|null
argument_list|)
return|;
block|}
name|List
argument_list|<
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
argument_list|>
name|r
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Account
operator|.
name|Id
name|a
range|:
name|args
operator|.
name|accounts
control|)
block|{
name|r
operator|.
name|add
argument_list|(
operator|new
name|EqualsLabelPredicate
argument_list|(
name|args
argument_list|,
name|label
argument_list|,
name|expVal
argument_list|,
name|a
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|or
argument_list|(
name|r
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|ChangeQueryBuilder
operator|.
name|FIELD_LABEL
operator|+
literal|":"
operator|+
name|value
return|;
block|}
block|}
end_class

end_unit

