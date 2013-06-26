begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.index
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
package|;
end_package

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
name|OperatorPredicate
import|;
end_import

begin_comment
comment|/** Index-aware predicate that includes a field type annotation. */
end_comment

begin_class
DECL|class|IndexPredicate
specifier|public
specifier|abstract
class|class
name|IndexPredicate
parameter_list|<
name|I
parameter_list|>
extends|extends
name|OperatorPredicate
argument_list|<
name|I
argument_list|>
block|{
DECL|field|def
specifier|private
specifier|final
name|FieldDef
argument_list|<
name|I
argument_list|,
name|?
argument_list|>
name|def
decl_stmt|;
DECL|method|IndexPredicate (FieldDef<I, ?> def, String value)
specifier|public
name|IndexPredicate
parameter_list|(
name|FieldDef
argument_list|<
name|I
argument_list|,
name|?
argument_list|>
name|def
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|def
operator|.
name|getName
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|this
operator|.
name|def
operator|=
name|def
expr_stmt|;
block|}
DECL|method|IndexPredicate (FieldDef<I, ?> def, String name, String value)
specifier|protected
name|IndexPredicate
parameter_list|(
name|FieldDef
argument_list|<
name|I
argument_list|,
name|?
argument_list|>
name|def
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
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|this
operator|.
name|def
operator|=
name|def
expr_stmt|;
block|}
DECL|method|getField ()
specifier|public
name|FieldDef
argument_list|<
name|I
argument_list|,
name|?
argument_list|>
name|getField
parameter_list|()
block|{
return|return
name|def
return|;
block|}
DECL|method|getType ()
specifier|public
name|FieldType
argument_list|<
name|?
argument_list|>
name|getType
parameter_list|()
block|{
return|return
name|def
operator|.
name|getType
argument_list|()
return|;
block|}
comment|/**    * @return whether this predicate can only be satisfied by looking at the    *     secondary index, i.e. it cannot be expressed as a query over the DB.    */
DECL|method|isIndexOnly ()
specifier|public
name|boolean
name|isIndexOnly
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

