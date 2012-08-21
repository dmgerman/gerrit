begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.rules
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|rules
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
name|ImmutableSet
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
name|extensions
operator|.
name|annotations
operator|.
name|ExtensionPoint
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|Predicate
import|;
end_import

begin_comment
comment|/**  * Provides additional packages that contain Prolog predicates that should be  * made available in the Prolog environment. The predicates can e.g. be used in  * the project submit rules.  *  * Each Java class defining a Prolog predicate must be in one of the provided  * packages and its name must apply to the 'PRED_[functor]_[arity]' format. In  * addition it must extend {@link Predicate}.  */
end_comment

begin_interface
annotation|@
name|ExtensionPoint
DECL|interface|PredicateProvider
specifier|public
interface|interface
name|PredicateProvider
block|{
comment|/** Return set of packages that contain Prolog predicates */
DECL|method|getPackages ()
specifier|public
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|getPackages
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

