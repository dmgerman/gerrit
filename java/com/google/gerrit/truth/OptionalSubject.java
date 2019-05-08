begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.truth
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|truth
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
name|Fact
operator|.
name|fact
import|;
end_import

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
name|assertAbout
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
name|truth
operator|.
name|CustomSubjectBuilder
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
name|truth
operator|.
name|DefaultSubject
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
name|truth
operator|.
name|FailureMetadata
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
name|truth
operator|.
name|StandardSubjectBuilder
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
name|truth
operator|.
name|Subject
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|BiFunction
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
import|;
end_import

begin_class
DECL|class|OptionalSubject
specifier|public
class|class
name|OptionalSubject
parameter_list|<
name|S
extends|extends
name|Subject
parameter_list|<
name|S
parameter_list|,
name|?
super|super
name|T
parameter_list|>
parameter_list|,
name|T
parameter_list|>
extends|extends
name|Subject
argument_list|<
name|OptionalSubject
argument_list|<
name|S
argument_list|,
name|T
argument_list|>
argument_list|,
name|Optional
argument_list|<
name|T
argument_list|>
argument_list|>
block|{
DECL|field|optional
specifier|private
specifier|final
name|Optional
argument_list|<
name|T
argument_list|>
name|optional
decl_stmt|;
DECL|field|valueSubjectCreator
specifier|private
specifier|final
name|BiFunction
argument_list|<
name|StandardSubjectBuilder
argument_list|,
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|S
argument_list|>
name|valueSubjectCreator
decl_stmt|;
comment|// TODO(aliceks): Remove when all relevant usages are adapted to new check()/factory approach.
DECL|method|assertThat ( Optional<T> optional, Function<? super T, ? extends S> elementAssertThatFunction)
specifier|public
specifier|static
parameter_list|<
name|S
extends|extends
name|Subject
argument_list|<
name|S
argument_list|,
name|T
argument_list|>
parameter_list|,
name|T
parameter_list|>
name|OptionalSubject
argument_list|<
name|S
argument_list|,
name|T
argument_list|>
name|assertThat
parameter_list|(
name|Optional
argument_list|<
name|T
argument_list|>
name|optional
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|S
argument_list|>
name|elementAssertThatFunction
parameter_list|)
block|{
name|Subject
operator|.
name|Factory
argument_list|<
name|S
argument_list|,
name|T
argument_list|>
name|valueSubjectFactory
init|=
parameter_list|(
name|metadata
parameter_list|,
name|value
parameter_list|)
lambda|->
name|elementAssertThatFunction
operator|.
name|apply
argument_list|(
name|value
argument_list|)
decl_stmt|;
return|return
name|assertThat
argument_list|(
name|optional
argument_list|,
name|valueSubjectFactory
argument_list|)
return|;
block|}
DECL|method|assertThat ( Optional<T> optional, Subject.Factory<S, T> valueSubjectFactory)
specifier|public
specifier|static
parameter_list|<
name|S
extends|extends
name|Subject
argument_list|<
name|S
argument_list|,
name|T
argument_list|>
parameter_list|,
name|T
parameter_list|>
name|OptionalSubject
argument_list|<
name|S
argument_list|,
name|T
argument_list|>
name|assertThat
parameter_list|(
name|Optional
argument_list|<
name|T
argument_list|>
name|optional
parameter_list|,
name|Subject
operator|.
name|Factory
argument_list|<
name|S
argument_list|,
name|T
argument_list|>
name|valueSubjectFactory
parameter_list|)
block|{
return|return
name|assertAbout
argument_list|(
name|optionals
argument_list|()
argument_list|)
operator|.
name|thatCustom
argument_list|(
name|optional
argument_list|,
name|valueSubjectFactory
argument_list|)
return|;
block|}
DECL|method|assertThat (Optional<?> optional)
specifier|public
specifier|static
name|OptionalSubject
argument_list|<
name|DefaultSubject
argument_list|,
name|?
argument_list|>
name|assertThat
parameter_list|(
name|Optional
argument_list|<
name|?
argument_list|>
name|optional
parameter_list|)
block|{
comment|// Unfortunately, we need to cast to DefaultSubject as StandardSubjectBuilder#that
comment|// only returns Subject<DefaultSubject, Object>. There shouldn't be a way
comment|// for that method not to return a DefaultSubject because the generic type
comment|// definitions of a Subject are quite strict.
return|return
name|assertAbout
argument_list|(
name|optionals
argument_list|()
argument_list|)
operator|.
name|that
argument_list|(
name|optional
argument_list|,
parameter_list|(
name|builder
parameter_list|,
name|value
parameter_list|)
lambda|->
operator|(
name|DefaultSubject
operator|)
name|builder
operator|.
name|that
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
DECL|method|optionals ()
specifier|public
specifier|static
name|CustomSubjectBuilder
operator|.
name|Factory
argument_list|<
name|OptionalSubjectBuilder
argument_list|>
name|optionals
parameter_list|()
block|{
return|return
name|OptionalSubjectBuilder
operator|::
operator|new
return|;
block|}
DECL|method|OptionalSubject ( FailureMetadata failureMetadata, Optional<T> optional, BiFunction<StandardSubjectBuilder, ? super T, ? extends S> valueSubjectCreator)
specifier|private
name|OptionalSubject
parameter_list|(
name|FailureMetadata
name|failureMetadata
parameter_list|,
name|Optional
argument_list|<
name|T
argument_list|>
name|optional
parameter_list|,
name|BiFunction
argument_list|<
name|StandardSubjectBuilder
argument_list|,
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|S
argument_list|>
name|valueSubjectCreator
parameter_list|)
block|{
name|super
argument_list|(
name|failureMetadata
argument_list|,
name|optional
argument_list|)
expr_stmt|;
name|this
operator|.
name|optional
operator|=
name|optional
expr_stmt|;
name|this
operator|.
name|valueSubjectCreator
operator|=
name|valueSubjectCreator
expr_stmt|;
block|}
DECL|method|isPresent ()
specifier|public
name|void
name|isPresent
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|optional
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|failWithoutActual
argument_list|(
name|fact
argument_list|(
literal|"expected to have"
argument_list|,
literal|"value"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isAbsent ()
specifier|public
name|void
name|isAbsent
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
if|if
condition|(
name|optional
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|failWithoutActual
argument_list|(
name|fact
argument_list|(
literal|"expected not to have"
argument_list|,
literal|"value"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isEmpty ()
specifier|public
name|void
name|isEmpty
parameter_list|()
block|{
name|isAbsent
argument_list|()
expr_stmt|;
block|}
DECL|method|value ()
specifier|public
name|S
name|value
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
name|isPresent
argument_list|()
expr_stmt|;
return|return
name|valueSubjectCreator
operator|.
name|apply
argument_list|(
name|check
argument_list|(
literal|"value()"
argument_list|)
argument_list|,
name|optional
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
DECL|class|OptionalSubjectBuilder
specifier|public
specifier|static
class|class
name|OptionalSubjectBuilder
extends|extends
name|CustomSubjectBuilder
block|{
DECL|method|OptionalSubjectBuilder (FailureMetadata failureMetadata)
name|OptionalSubjectBuilder
parameter_list|(
name|FailureMetadata
name|failureMetadata
parameter_list|)
block|{
name|super
argument_list|(
name|failureMetadata
argument_list|)
expr_stmt|;
block|}
DECL|method|thatCustom ( Optional<T> optional, Subject.Factory<S, T> valueSubjectFactory)
specifier|public
parameter_list|<
name|S
extends|extends
name|Subject
argument_list|<
name|S
argument_list|,
name|T
argument_list|>
parameter_list|,
name|T
parameter_list|>
name|OptionalSubject
argument_list|<
name|S
argument_list|,
name|T
argument_list|>
name|thatCustom
parameter_list|(
name|Optional
argument_list|<
name|T
argument_list|>
name|optional
parameter_list|,
name|Subject
operator|.
name|Factory
argument_list|<
name|S
argument_list|,
name|T
argument_list|>
name|valueSubjectFactory
parameter_list|)
block|{
return|return
name|that
argument_list|(
name|optional
argument_list|,
parameter_list|(
name|builder
parameter_list|,
name|value
parameter_list|)
lambda|->
name|builder
operator|.
name|about
argument_list|(
name|valueSubjectFactory
argument_list|)
operator|.
name|that
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
DECL|method|that (Optional<?> optional)
specifier|public
name|OptionalSubject
argument_list|<
name|DefaultSubject
argument_list|,
name|?
argument_list|>
name|that
parameter_list|(
name|Optional
argument_list|<
name|?
argument_list|>
name|optional
parameter_list|)
block|{
return|return
name|that
argument_list|(
name|optional
argument_list|,
parameter_list|(
name|builder
parameter_list|,
name|value
parameter_list|)
lambda|->
operator|(
name|DefaultSubject
operator|)
name|builder
operator|.
name|that
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
DECL|method|that ( Optional<T> optional, BiFunction<StandardSubjectBuilder, ? super T, ? extends S> valueSubjectCreator)
specifier|public
parameter_list|<
name|S
extends|extends
name|Subject
argument_list|<
name|S
argument_list|,
name|?
super|super
name|T
argument_list|>
parameter_list|,
name|T
parameter_list|>
name|OptionalSubject
argument_list|<
name|S
argument_list|,
name|T
argument_list|>
name|that
parameter_list|(
name|Optional
argument_list|<
name|T
argument_list|>
name|optional
parameter_list|,
name|BiFunction
argument_list|<
name|StandardSubjectBuilder
argument_list|,
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|S
argument_list|>
name|valueSubjectCreator
parameter_list|)
block|{
return|return
operator|new
name|OptionalSubject
argument_list|<>
argument_list|(
name|metadata
argument_list|()
argument_list|,
name|optional
argument_list|,
name|valueSubjectCreator
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

